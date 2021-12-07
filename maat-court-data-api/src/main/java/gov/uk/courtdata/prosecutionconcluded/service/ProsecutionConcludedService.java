package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.crowncourt.OffenceSummary;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.CalculateCrownCourtOutcome;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcludedValidator;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateCrownCourtOutcome calculateCrownCourtOutcome;

    private final WQHearingRepository wqHearingRepository;

    private final ProsecutionConcludedValidator prosecutionConcludedValidator;

    private final ProsecutionConcludedDAO prosecutionConcludedDAO;

    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        Optional<WQHearingEntity> optionalWQHearingEntity = wqHearingRepository.findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(),prosecutionConcluded.getHearingIdWhereChangeOccured().toString());

        if (optionalWQHearingEntity.isEmpty())
            throw new MAATCourtDataException("Hearing not found for this hearingId " + prosecutionConcluded.getProsecutionCaseId());

        WQHearingEntity wqHearingEntity = optionalWQHearingEntity.get();

        if (JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

            prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());

            String calculatedOutcome = calculateCrownCourtOutcome.calculate(prosecutionConcluded);
            log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

            prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());

            ConcludedDTO concludedDTO = ConcludedDTO.
                    builder()
                    .prosecutionConcluded(prosecutionConcluded)
                    .calculatedOutcome(calculatedOutcome)
                    .ouCourtLocation(wqHearingEntity.getOuCourtLocation())
                    .wqJurisdictionType(wqHearingEntity.getWqJurisdictionType())
                    //TODO: appeal type
                    .appealType("AR")
                    .caseEndDate(getMostRecentCaseEndDate(prosecutionConcluded.getOffenceSummaryList()))
                    .caseUrn(wqHearingEntity.getCaseUrn())
                    .hearingResultCode(buildResultCodeList(wqHearingEntity))
                    .build();

            prosecutionConcludedDAO.execute(concludedDTO);
        }
    }

    private String getMostRecentCaseEndDate(List<OffenceSummary> offenceSummaryList) {

        if(offenceSummaryList.isEmpty())
            return null;

        return offenceSummaryList.stream()
                .map(offenceSummary -> LocalDate.parse(offenceSummary.getProceedingsConcludedChangedDate()))
                .distinct()
                .collect(Collectors.toList())
                .stream().sorted()
                .collect(Collectors.toList()).get(0).toString();
    }
    private List<String> buildResultCodeList(WQHearingEntity wqHearingEntity) {
        return Arrays.stream(wqHearingEntity.getResultCodes().split(","))
            .distinct()
            .collect(Collectors.toList());
    }
}
