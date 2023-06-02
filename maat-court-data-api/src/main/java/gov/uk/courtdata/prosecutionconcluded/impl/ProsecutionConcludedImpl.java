package gov.uk.courtdata.prosecutionconcluded.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CrownCourtCodeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ResultCodeHelper;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.isAppeal;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForAppeal;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForTrial;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isTrial;

@Component
@XRayEnabled
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedImpl {

    private final RepOrderRepository repOrderRepository;

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    private final CrownCourtCodeHelper crownCourtCodeHelper;

    private final ProcessSentencingImpl processSentencingHelper;

    private final ResultCodeHelper resultCodeHelper;

    public void execute(ConcludedDTO concludedDTO) {

        Integer maatId = concludedDTO.getProsecutionConcluded().getMaatId();
        final Optional<RepOrderEntity> optionalRepEntity = repOrderRepository.findById(maatId);
        log.debug("Maat-id found and processing ProsecutionConcluded");
        if (optionalRepEntity.isPresent()) {

            RepOrderEntity repOrderEntity = optionalRepEntity.get();

            verifyCaseTypeValidator(repOrderEntity,concludedDTO.getCalculatedOutcome());

            crownCourtStoredProcedureRepository
                    .updateCrownCourtOutcome(
                            maatId,
                            concludedDTO.getCalculatedOutcome(),
                            resultCodeHelper.isBenchWarrantIssued(concludedDTO.getCalculatedOutcome(), concludedDTO.getHearingResultCodeList()),
                            repOrderEntity.getAppealTypeCode(),
                            resultCodeHelper.isImprisoned(concludedDTO.getCalculatedOutcome(), concludedDTO.getHearingResultCodeList()),
                            concludedDTO.getCaseUrn(),
                            crownCourtCodeHelper.getCode(concludedDTO.getOuCourtLocation()));

            processSentencingHelper.processSentencingDate(concludedDTO.getCaseEndDate(), maatId, repOrderEntity.getCatyCaseType());
        }
    }

    private void verifyCaseTypeValidator(RepOrderEntity repOrder, String calculatedOutcome) {

        String caseType = repOrder.getCatyCaseType();
        log.debug("Crown Court - verifying case Type validator");
        if (isTrial(calculatedOutcome) && !caseTypeForTrial(caseType)) {

            throw new ValidationException("Crown Court - Case type not valid for Trial.");
        }

        if (isAppeal(calculatedOutcome) && !caseTypeForAppeal(caseType)) {

            throw new ValidationException("Crown Court  - Case type not valid for Appeal.");
        }
    }

}