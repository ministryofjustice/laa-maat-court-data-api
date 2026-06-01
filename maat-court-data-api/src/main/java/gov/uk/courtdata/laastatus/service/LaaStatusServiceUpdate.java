package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laastatus.builder.CourtDataDTOBuilder;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.laastatus.AutoLaaStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaaStatusServiceUpdate {

    private final LaaStatusService laaStatusService;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final CourtDataDTOBuilder courtDataDTOBuilder;

    public void updateMlaAndCDA(CaseDetails caseDetails) {

        CourtDataDTO courtDataDTO = courtDataDTOBuilder.build(caseDetails);

        processLaaStatusServiceForCDA(courtDataDTO);
        processLaaStatusServiceForMLA(courtDataDTO);
    }

    private void processLaaStatusServiceForCDA(CourtDataDTO courtDataDTO) {
        log.info("Start - POST Rep Order update to CDA");
        laaStatusPostCDAService.process(courtDataDTO);
        log.info("Ends - POST Rep Order update to CDA");
    }

    private void processLaaStatusServiceForMLA(CourtDataDTO courtDataDTO) {

        if (!courtDataDTO.getCaseDetails().isOnlyForCDAService()) {
            log.info("Start - Update LAA status to MLA");
            laaStatusService.execute(courtDataDTO);
            log.info("Ends - After laa update to MLA");
        }
    }

    public void autoLAAStatusUpdate(CourtDataDTO courtDataDTO) {
        log.info("Start - Auto LAA status update");
        Integer repId = Optional.ofNullable(courtDataDTO)
                .map(CourtDataDTO::getCaseDetails)
                .map(CaseDetails::getMaatId)
                .orElse(null);

        if (repId == null) {
            log.warn("Maat id is null in CourtDataDTO");
            return;
        }

        List<Offence> offenceList = Optional.ofNullable(courtDataDTO.getCaseDetails())
                .map(CaseDetails::getDefendant)
                .map(defendant -> defendant.getOffences())
                .orElse(null);

        if (offenceList == null || offenceList.isEmpty()) {
            log.warn("No offences found for repId: {}", repId);
            return;
        }

        log.info("Auto LAA status update for Maat id: {}", repId);

        for (Offence offence : offenceList) {
            try {
                AutoLaaStatusUpdate previousLaaStatusUpdate = findPreviousLAAStatus(repId, offence.getOffenceCode());
                if (previousLaaStatusUpdate == null) {
                    log.info(
                            "No previous LAA status found for repId: {}, offence code: {}",
                            repId,
                            offence.getOffenceCode());
                    continue;
                }
                mapAutoLaaStatusToOffence(offence, previousLaaStatusUpdate);

                List<WqLinkRegisterEntity> linkedList = laaStatusService.getLinkingDetails(repId);
                if (linkedList == null || linkedList.isEmpty()) {
                    log.info("No linking records found for repId: {}", repId);
                    continue;
                }

                log.info(
                        "Previous status is found status {}, IOJ {} and date {}",
                        previousLaaStatusUpdate.getLegalAidStatus(),
                        previousLaaStatusUpdate.getIojDecision(),
                        previousLaaStatusUpdate.getLegalAidStatusDate());
                WqLinkRegisterEntity linked = linkedList.get(0);

                Optional<OffenceEntity> linkedOffences =
                        laaStatusService.getOffenceDetails(linked.getCaseId(), offence.getOffenceCode());
                Optional<CaseEntity> linkedCases =
                        laaStatusService.getCaseDetails(linked.getCaseId(), linked.getCreatedTxId());

                if (linkedOffences == null || linkedOffences.isEmpty()) {
                    log.info("No offence details found for linking record, repId: {}", repId);
                    continue;
                }
                log.info("Updating previous status {}", previousLaaStatusUpdate);
                updateLinkedEntities(linked, linkedOffences.get(), linkedCases.get(), previousLaaStatusUpdate);
                if (previousLaaStatusUpdate.getLegalAidStatus() != null
                        && previousLaaStatusUpdate.getLegalAidStatus().equals("AP")) {
                    processLaaStatusServiceForCDA(courtDataDTO);
                }
            } catch (Exception e) {
                log.error(
                        "Error processing auto LAA status update for repId: {}, offenceCode: {}. Error: {}",
                        repId,
                        offence.getOffenceCode(),
                        e.getMessage(),
                        e);
            }
        }
        log.info("Ends - Auto LAA status update");
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
            laaStatusService.saveOffence(linkedOffence);
        }

        // Update and save mlrCat if present
        if (previousLaaStatusUpdate.getMlrCat() != null) {
            linked.setMlrCat(previousLaaStatusUpdate.getMlrCat());
            laaStatusService.saveLink(linked);
        }

        // Update and save cjsAreaCode if present
        if (linkedCase != null && previousLaaStatusUpdate.getCjsAreaCode() != null) {
            linkedCase.setCjsAreaCode(previousLaaStatusUpdate.getCjsAreaCode());
            laaStatusService.saveCase(linkedCase);
        }
    }

    public AutoLaaStatusUpdate findPreviousLAAStatus(Integer repId, String offenceCode) {

        AutoLaaStatusUpdate previousLaaStatusUpdate = new AutoLaaStatusUpdate();
        Optional<WqLinkRegisterEntity> unlinkedList = laaStatusService.getUnLinkDetails(repId);
        if (unlinkedList.isEmpty()) {
            log.info("No Previous unlinked records found for repId: {}", repId);
            return null;
        }
        WqLinkRegisterEntity unlinked = unlinkedList.get();

        Optional<OffenceEntity> unlinkedOffences =
                laaStatusService.getOffenceDetails(unlinked.getCaseId(), offenceCode);
        Optional<CaseEntity> unlinkedCases =
                laaStatusService.getCaseDetails(unlinked.getCaseId(), unlinked.getCreatedTxId());
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
        if (unlinkedOffences != null || !unlinkedOffences.isEmpty()) {
            previousLaaStatusUpdate.setCjsAreaCode(unlinkedCase.getCjsAreaCode());
        }
        return previousLaaStatusUpdate;
    }
}
