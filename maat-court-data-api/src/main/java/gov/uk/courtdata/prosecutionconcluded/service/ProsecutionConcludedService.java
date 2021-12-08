package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.CalculateCrownCourtOutcome;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcludedValidator;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateCrownCourtOutcome calculateCrownCourtOutcome;

    private final WQHearingRepository wqHearingRepository;

    private final ProsecutionConcludedValidator prosecutionConcludedValidator;

    private final ProsecutionConcludedImpl prosecutionConcludedImpl;

    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        String calculatedOutcome = calculateCrownCourtOutcome.calculate(prosecutionConcluded);
        log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

        WQHearingEntity wqHearingEntity = wqHearingRepository
                .findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(), prosecutionConcluded.getHearingIdWhereChangeOccured().toString())
                .orElseThrow(() ->
                        new MAATCourtDataException("Hearing not found for this hearingId " + prosecutionConcluded.getProsecutionCaseId()));

        if (JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

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

            prosecutionConcludedImpl.execute(concludedDTO);
        }
    }

    private String getMostRecentCaseEndDate(List<OffenceSummary> offenceSummaryList) {

        if(offenceSummaryList.isEmpty())
            return null;

        return offenceSummaryList.stream()
                .map(offenceSummary -> LocalDate.parse(offenceSummary.getProceedingsConcludedChangedDate()))
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList()).get(0).toString();
    }
    private List<String> buildResultCodeList(WQHearingEntity wqHearingEntity) {
        return Arrays.stream(wqHearingEntity.getResultCodes().split(","))
            .distinct()
            .collect(Collectors.toList());
    }
}
