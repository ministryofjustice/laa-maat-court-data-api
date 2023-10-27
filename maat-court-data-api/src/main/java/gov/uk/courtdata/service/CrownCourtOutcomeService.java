package gov.uk.courtdata.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.UpdateCCOutcome;
import gov.uk.courtdata.model.UpdateSentenceOrder;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
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

    private final CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;
    private final CrownCourtProcessingRepository crownCourtProcessingRepository;

    @Transactional
    public void updateCCOutcome(final UpdateCCOutcome updateCCOutcome) {
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

    public void updateCCSentenceOrderDate(UpdateSentenceOrder updateSentenceOrder) {
        log.info("Calling update_cc_sentence_order_dt Stored Procedure- Start");
        crownCourtProcessingRepository.invokeUpdateSentenceOrderDate(updateSentenceOrder.getRepId(),
                updateSentenceOrder.getDbUser(),
                updateSentenceOrder.getSentenceOrderDate()
        );
        log.info("Calling update_cc_sentence_order_dt Stored Procedure - End");
    }

    public void updateAppealCCSentenceOrderDate(UpdateSentenceOrder updateSentenceOrder) {
        log.info("Calling update_appeal_sentence_ord_dt Stored Procedure- Start");
        crownCourtProcessingRepository.invokeUpdateAppealSentenceOrderDate(updateSentenceOrder.getRepId(),
                updateSentenceOrder.getDbUser(),
                updateSentenceOrder.getSentenceOrderDate(),
                updateSentenceOrder.getDateChanged()
        );
        log.info("Calling update_appeal_sentence_ord_dt Stored Procedure - End");
    }
}
