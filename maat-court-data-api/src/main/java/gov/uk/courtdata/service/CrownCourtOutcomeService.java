package gov.uk.courtdata.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.UpdateCCOutcome;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class CrownCourtOutcomeService {

    CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    @Transactional
    public void update(final UpdateCCOutcome updateCCOutcome) {
        log.info("Calling update CC Outcome Stored Procedure- Start");
        crownCourtStoredProcedureRepository.updateCrownCourtOutcome(updateCCOutcome.getRepId(),
                updateCCOutcome.getCcOutcome(),
                updateCCOutcome.getBenchWarrantIssued(),
                updateCCOutcome.getAppealType(),
                updateCCOutcome.getImprisoned(),
                updateCCOutcome.getCaseNumber(),
                updateCCOutcome.getCrownCourtCode()
        );
        log.info("Calling update CC Outcome Stored Procedure - End");
    }
}
