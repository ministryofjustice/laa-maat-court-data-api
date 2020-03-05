package gov.uk.courtdata.link.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.link.processor.*;
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
    public void execute(CourtDataDTO courtDataDTO) {

        mapIdentifiers(courtDataDTO);
        caseInfoProcessor.process(courtDataDTO);
        wqCoreInfoProcessor.process(courtDataDTO);
        wqLinkRegisterProcessor.process(courtDataDTO);
        solicitorInfoProcessor.process(courtDataDTO);
        proceedingsInfoProcessor.process(courtDataDTO);
        defendantInfoProcessor.process(courtDataDTO);
        sessionInfoProcessor.process(courtDataDTO);
        offenceInfoProcessor.process(courtDataDTO);
        resultsInfoProcessor.process(courtDataDTO);
        repOrderInfoProcessor.process(courtDataDTO);

    }

    private void mapIdentifiers(CourtDataDTO courtDataDTO) {
        courtDataDTO.setTxId(identifierRepository.getTxnID());
        courtDataDTO.setLibraId(identifierRepository.getLibraID());
        courtDataDTO.setProceedingId(identifierRepository.getProceedingID());
        courtDataDTO.setCaseId(identifierRepository.getCaseID());
    }

}
