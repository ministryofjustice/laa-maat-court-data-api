package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.entity.DlrmStatusUpdateEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidator;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.laastatus.AutoLaaStatusUpdate;
import gov.uk.courtdata.repository.CaseRepository;
import gov.uk.courtdata.repository.DlrmStatusUpdateRepository;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DlrmStatusUpdateService {

    private final LaaStatusService laaStatusService;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final LaaStatusValidator laaStatusValidator;

    private final DlrmStatusUpdateRepository dlrmStatusUpdateRepository;

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    private final OffenceRepository offenceRepository;

    private final CaseRepository caseRepository;

    public void autoLAAStatusUpdate(CourtDataDTO courtDataDTO) {
        log.info("Start - Auto LAA status update");
        Integer repId = extractRepId(courtDataDTO);
        if (repId == null) {
            log.warn("Maat id is null in CourtDataDTO");
            return;
        }

        List<Offence> offenceList = extractOffenceList(courtDataDTO);

        if (offenceList == null || offenceList.isEmpty()) {
            log.warn("No offences found for repId: {}", repId);
            return;
        }

        log.info("Auto LAA status update for Maat id: {}", repId);

        Map<Offence, String> offenceResultMap = offenceList.stream()
                .collect(Collectors.toMap(
                        offence -> offence, offence -> processOffenceForAutoLaaStatus(repId, offence)));

        // Step 2: Save DLRM status update for offences with non-null result
        offenceList.forEach(offence -> {
            String result = offenceResultMap.get(offence);
            if (result != null) {
                saveDlrmStatusUpdate(repId, offence.getOffenceId(), offence.getOffenceCode(), result);
            }
        });

        // Step 3: Check eligibility (any offence with null result)
        boolean hasEligibleOffence = offenceResultMap.values().stream().anyMatch(result -> result == null);

        if (!hasEligibleOffence) {
            log.info("No previous status available to restore for repId: {}", repId);
            saveDlrmStatusUpdate(
                    repId,
                    "",
                    "",
                    String.format(
                            "No previous status available to restore for repId: %d and skipping laa status update to CDA",
                            repId));
            log.info("Ends - Auto LAA status update");
            return;
        }

        List<Offence> filteredOffences = filterValidOffences(offenceList);

        if (filteredOffences.isEmpty()) {
            log.info("No offences eligible for LAA status update for repId: {}", repId);
            saveDlrmStatusUpdate(
                    repId,
                    "",
                    "",
                    String.format(
                            "No offences eligible for LAA status update and skipping laa status update call to CDA"));
            log.info("Ends - Auto LAA status update");
            return;
        }
        assignFilteredOffencesToCourtDataDTO(courtDataDTO, filteredOffences);

        processLaaStatusServiceForCDA(courtDataDTO);

        log.info("Ends - Auto LAA status update");
    }

    private String processOffenceForAutoLaaStatus(Integer repId, Offence offence) {
        log.info("Processing offence with code: {} for repId: {}", offence.getOffenceCode(), repId);
        try {
            AutoLaaStatusUpdate previousLaaStatusUpdate = findPreviousLAAStatus(repId, offence.getOffenceCode());
            if (previousLaaStatusUpdate == null) {
                log.info(
                        "No previous LAA status found for repId: {}, offence code: {}",
                        repId,
                        offence.getOffenceCode());
                return String.format(
                        "No previous LAA status found for repId: %d, offence code: %s",
                        repId, offence.getOffenceCode());
            }
            mapAutoLaaStatusToOffence(offence, previousLaaStatusUpdate);

            List<WqLinkRegisterEntity> linkedList = getLinkingDetails(repId);
            if (linkedList == null || linkedList.isEmpty()) {
                log.info("No linking records found for repId: {}", repId);
                return String.format("No linking records found for repId: %d", repId);
            }

            log.info(
                    "Previous status is found status {}, IOJ {} and date {} for repId: {} and offence code: {}",
                    previousLaaStatusUpdate.getLegalAidStatus(),
                    previousLaaStatusUpdate.getIojDecision(),
                    previousLaaStatusUpdate.getLegalAidStatusDate(),
                    repId,
                    offence.getOffenceCode());

            WqLinkRegisterEntity linked = linkedList.get(0);

            Optional<OffenceEntity> linkedOffences = getOffenceDetails(linked.getCaseId(), offence.getOffenceCode());
            Optional<CaseEntity> linkedCases = getCaseDetails(linked.getCaseId(), linked.getCreatedTxId());

            if (linkedOffences == null || linkedOffences.isEmpty()) {
                log.info(
                        "No offence details found for linking record, repId: {} and offence code: {}",
                        repId,
                        offence.getOffenceCode());
                return String.format(
                        "No offence details found for linking record, repId: %d, , offence code: %s",
                        repId, offence.getOffenceCode());
            }
            log.info("Updating previous status {}", previousLaaStatusUpdate);
            updateLinkedEntities(linked, linkedOffences.get(), linkedCases.orElse(null), previousLaaStatusUpdate);
            log.info(
                    "Successfully updated previous status for repId: {} and offence code: {}",
                    repId,
                    offence.getOffenceCode());
            return null;
        } catch (Exception e) {
            log.error(
                    "Error processing auto LAA status update for repId: {}, offenceCode: {}. Error: {}",
                    repId,
                    offence.getOffenceCode(),
                    e.getMessage(),
                    e);
            return String.format(
                    "Error processing auto LAA status update for repId: {} and  offenceCode: {}",
                    repId,
                    offence.getOffenceCode());
        }
    }

    private Integer extractRepId(CourtDataDTO courtDataDTO) {
        return Optional.ofNullable(courtDataDTO)
                .map(CourtDataDTO::getCaseDetails)
                .map(CaseDetails::getMaatId)
                .orElse(null);
    }

    private List<Offence> extractOffenceList(CourtDataDTO courtDataDTO) {
        return Optional.ofNullable(courtDataDTO.getCaseDetails())
                .map(CaseDetails::getDefendant)
                .map(defendant -> defendant.getOffences())
                .orElse(null);
    }

    private void mapAutoLaaStatusToOffence(Offence offence, AutoLaaStatusUpdate previousLaaStatusUpdate) {
        offence.setLegalAidStatus(previousLaaStatusUpdate.getLegalAidStatus());
        offence.setIojDecision(previousLaaStatusUpdate.getIojDecision());
        Optional.ofNullable(previousLaaStatusUpdate.getLegalAidStatusDate())
                .map(Object::toString)
                .ifPresent(offence::setLegalAidStatusDate);
    }

    private void updateLinkedEntities(
            WqLinkRegisterEntity linked,
            OffenceEntity linkedOffence,
            CaseEntity linkedCase,
            AutoLaaStatusUpdate previousLaaStatusUpdate) {

        boolean canUpdateOffence = false;

        // Update LegalAidStatus if present
        if (previousLaaStatusUpdate.getLegalAidStatus() != null
                && !previousLaaStatusUpdate.getLegalAidStatus().equals("AP")) {
            linkedOffence.setLegalAidStatus(previousLaaStatusUpdate.getLegalAidStatus());
            canUpdateOffence = true;
        }

        // Update legalAidStatusDate if present
        if (previousLaaStatusUpdate.getLegalAidStatusDate() != null) {
            linkedOffence.setLegalAidStatusDate(previousLaaStatusUpdate.getLegalAidStatusDate());
            canUpdateOffence = true;
        }

        // Update iojDecision if present
        if (previousLaaStatusUpdate.getIojDecision() != null) {
            linkedOffence.setIojDecision(previousLaaStatusUpdate.getIojDecision());
            canUpdateOffence = true;
        }

        // Save offence if updated
        if (canUpdateOffence) {
            saveOffence(linkedOffence);
        }

        // Update and save mlrCat if present
        if (previousLaaStatusUpdate.getMlrCat() != null) {
            linked.setMlrCat(previousLaaStatusUpdate.getMlrCat());
            saveLink(linked);
        }

        // Update and save cjsAreaCode if present
        if (linkedCase != null && previousLaaStatusUpdate.getCjsAreaCode() != null) {
            linkedCase.setCjsAreaCode(previousLaaStatusUpdate.getCjsAreaCode());
            saveCase(linkedCase);
        }
    }

    public AutoLaaStatusUpdate findPreviousLAAStatus(Integer repId, String offenceCode) {
        log.info("Start - Find previous LAA status for repId: {}, offence code: {}", repId, offenceCode);

        AutoLaaStatusUpdate previousLaaStatusUpdate = new AutoLaaStatusUpdate();
        Optional<WqLinkRegisterEntity> unlinkedList = getUnLinkDetails(repId);
        if (unlinkedList.isEmpty()) {
            log.info("No Previous unlinked records found for repId: {}", repId);
            return null;
        }
        WqLinkRegisterEntity unlinked = unlinkedList.get();

        Optional<OffenceEntity> unlinkedOffences = getOffenceDetails(unlinked.getCaseId(), offenceCode);
        Optional<CaseEntity> unlinkedCases = getCaseDetails(unlinked.getCaseId(), unlinked.getCreatedTxId());
        if (unlinkedOffences == null || unlinkedOffences.isEmpty()) {
            log.info("No Previous offence found for unlinked record, repId: {}", repId);
            return null;
        }
        OffenceEntity unlinkedOffence = unlinkedOffences.get();

        previousLaaStatusUpdate.setLegalAidStatus(unlinkedOffence.getLegalAidStatus());
        previousLaaStatusUpdate.setLegalAidStatusDate(unlinkedOffence.getLegalAidStatusDate());
        previousLaaStatusUpdate.setIojDecision(unlinkedOffence.getIojDecision());
        previousLaaStatusUpdate.setMlrCat(unlinked.getMlrCat());

        CaseEntity unlinkedCase = unlinkedCases.get();
        if (unlinkedCase != null) {
            previousLaaStatusUpdate.setCjsAreaCode(unlinkedCase.getCjsAreaCode());
        }
        log.info("End - Find previous LAA status ");
        return previousLaaStatusUpdate;
    }

    private List<Offence> filterValidOffences(List<Offence> offenceList) {
        return offenceList.stream()
                .filter(offence -> {
                    String result = laaStatusValidator.validateLAAStatus(offence);
                    if (result != null && !result.isEmpty()) {
                        log.info("Validation result for offence code {}: {}", offence.getOffenceCode(), result);
                    }
                    return result == null || result.isEmpty();
                })
                .collect(Collectors.toList());
    }

    private void assignFilteredOffencesToCourtDataDTO(CourtDataDTO courtDataDTO, List<Offence> filteredOffences) {
        if (courtDataDTO.getCaseDetails() != null
                && courtDataDTO.getCaseDetails().getDefendant() != null) {
            courtDataDTO.getCaseDetails().getDefendant().setOffences(filteredOffences);
        }
    }

    private void processLaaStatusServiceForCDA(CourtDataDTO courtDataDTO) {
        log.info("Start - POST Rep Order update to CDA");
        laaStatusPostCDAService.process(courtDataDTO);
        log.info("Ends - POST Rep Order update to CDA");
    }

    private void saveDlrmStatusUpdate(Integer repId, String offenceId, String offenceCode, String status) {
        DlrmStatusUpdateEntity dlrmUpdateStatusEntity = new DlrmStatusUpdateEntity();
        dlrmUpdateStatusEntity.setMaatId(repId);
        dlrmUpdateStatusEntity.setOffenceCode(offenceCode);
        dlrmUpdateStatusEntity.setOffenceId(offenceId);
        dlrmUpdateStatusEntity.setErrorMessage(status);
        dlrmStatusUpdateRepository.saveAndFlush(dlrmUpdateStatusEntity);
    }

    public Optional<WqLinkRegisterEntity> getUnLinkDetails(Integer repId) {
        return wqLinkRegisterRepository.findUnlinkByMaat(repId);
    }

    public Optional<OffenceEntity> getOffenceDetails(Integer caseId, String offenceCode) {
        return offenceRepository.getOffenceDetail(caseId, offenceCode);
    }

    public List<WqLinkRegisterEntity> getLinkingDetails(Integer repId) {
        return wqLinkRegisterRepository.findBymaatId(repId);
    }

    public Optional<CaseEntity> getCaseDetails(Integer caseId, Integer txId) {
        return caseRepository.getCaseDetail(caseId, txId);
    }

    @Transactional
    public OffenceEntity saveOffence(OffenceEntity wq) {
        return offenceRepository.save(wq);
    }

    @Transactional
    public WqLinkRegisterEntity saveLink(WqLinkRegisterEntity wq) {
        return wqLinkRegisterRepository.save(wq);
    }

    @Transactional
    public CaseEntity saveCase(CaseEntity wq) {
        return caseRepository.save(wq);
    }
}
