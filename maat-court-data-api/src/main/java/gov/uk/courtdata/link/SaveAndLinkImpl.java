package gov.uk.courtdata.link;

import gov.uk.courtdata.link.processor.*;
import gov.uk.courtdata.model.SaveAndLinkModel;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveAndLinkImpl {

    private final CaseInfoProcessor caseInfoProcessor;
    private final WqCoreInfoProcessor wqCoreInfoProcessor;
    private final WqLinkRegisterProcessor wqLinkRegisterProcessor;
    private final SolicitorInfoProcessor solicitorInfoProcessor;
    private final ProceedingsInfoProcessor proceedingsInfoProcessor;
    private final DefendantInfoProcessor defendantInfoProcessor;
    private final SessionInfoProcessor sessionInfoProcessor;
    private final OffenceInfoProcessor offenceInfoProcessor;
    private final ResultsInfoProcessor resultsInfoProcessor;
    private final RepOrderInfoProcessor repOrderInfoProcessor;
    private final IdentifierRepository identifierRepository;

    @Transactional
    public void execute(SaveAndLinkModel saveAndLinkModel) {

        mapIdentifiers(saveAndLinkModel);
        caseInfoProcessor.process(saveAndLinkModel);
        wqCoreInfoProcessor.process(saveAndLinkModel);
        wqLinkRegisterProcessor.process(saveAndLinkModel);
        solicitorInfoProcessor.process(saveAndLinkModel);
        proceedingsInfoProcessor.process(saveAndLinkModel);
        defendantInfoProcessor.process(saveAndLinkModel);
        sessionInfoProcessor.process(saveAndLinkModel);
        offenceInfoProcessor.process(saveAndLinkModel);
        resultsInfoProcessor.process(saveAndLinkModel);
        repOrderInfoProcessor.process(saveAndLinkModel);

    }

    private void mapIdentifiers(SaveAndLinkModel saveAndLinkModel) {
        saveAndLinkModel.setTxId(identifierRepository.getTxnID());
        saveAndLinkModel.setLibraId(identifierRepository.getLibraID());
        saveAndLinkModel.setProceedingId(identifierRepository.getProceedingID());
        saveAndLinkModel.setCaseId(identifierRepository.getCaseID());
    }

}
