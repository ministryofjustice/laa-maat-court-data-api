package gov.uk.courtdata.laaStatus.impl;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.laaStatus.processor.UpdateDefendantInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqCoreInfoProcessor;
import gov.uk.courtdata.link.processor.*;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class LaaStatusUpdateImpl {

    private final IdentifierRepository identifierRepository;
    private final CaseInfoProcessor caseInfoProcessor;
    private final UpdateWqCoreInfoProcessor updateWqCoreInfoProcessor;
    private final WqLinkRegisterProcessor wqLinkRegisterProcessor;
    private final SolicitorInfoProcessor solicitorInfoProcessor;
    private final ProceedingsInfoProcessor proceedingsInfoProcessor;
    private final UpdateDefendantInfoProcessor updateDefendantInfoProcessor;
    private final SessionInfoProcessor sessionInfoProcessor;
    private final OffenceInfoProcessor offenceInfoProcessor;


    @Transactional
    public void execute(LaaModelManager laaModelManager) {

        mapTxnID(laaModelManager);
        caseInfoProcessor.process(laaModelManager);
        updateWqCoreInfoProcessor.process(laaModelManager);
        wqLinkRegisterProcessor.process(laaModelManager);
        solicitorInfoProcessor.process(laaModelManager);
        proceedingsInfoProcessor.process(laaModelManager);
        updateDefendantInfoProcessor.process(laaModelManager);
        sessionInfoProcessor.process(laaModelManager);
        offenceInfoProcessor.process(laaModelManager);

    }

    private void mapTxnID(LaaModelManager laaModelManager) {

        laaModelManager.setTxId(identifierRepository.getTxnID());

    }
}
