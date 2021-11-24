package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.crowncourt.validator.CaseTypeValidator;
import gov.uk.courtdata.hearing.crowncourt.validator.OUCodeValidator;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.CalculateCrownCourtOutcome;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateCrownCourtOutcome calculateCrownCourtOutcome;

    private final WQHearingRepository wqHearingRepository;

    private final OUCodeValidator ouCodeValidator;
    private final CaseTypeValidator caseTypeValidator;

    private final ProsecutionConcludedDAO prosecutionConcludedDAO;

    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        WQHearingEntity wqHearingEntity = wqHearingRepository.getById(prosecutionConcluded.getProsecutionCaseId().toString());

//        if (Optional.ofNullable(wqHearingEntity).isEmpty())
//            throw new MAATCourtDataException("Hearing not found for this hearingId " + prosecutionConcluded.getProsecutionCaseId());

        if (JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

            String calculatedOutcome = calculateCrownCourtOutcome.calculate(prosecutionConcluded);
            log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

            //Todo: create or update the existing classes to process this validation
            //ouCodeValidator.validate(wqHearingEntity.getOuCourtLocation());
            //caseTypeValidator.validate(maatId, calculatedOutcome);

            ConcludedDTO concludedDTO = ConcludedDTO.
                    builder()
                    .prosecutionConcluded(prosecutionConcluded)
                    .calculatedOutcome(calculatedOutcome)
                    .ouCourtLocation(wqHearingEntity.getOuCourtLocation())
                    .wqJurisdictionType(wqHearingEntity.getWqJurisdictionType())
                    .appealType("")
                    .caseEndDate("")
                    .caseUrn("")
                    .build();

            prosecutionConcludedDAO.execute(concludedDTO);

        }
    }






}
