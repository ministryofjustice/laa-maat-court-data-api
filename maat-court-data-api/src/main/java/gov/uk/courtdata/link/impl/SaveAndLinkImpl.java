package gov.uk.courtdata.link.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.link.processor.*;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
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

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void execute(CourtDataDTO courtDataDTO) {
        log.info("Create Link - Transaction Processing - Start");
        mapIdentifiers(courtDataDTO);
        caseInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Case Details are Processed");
        wqCoreInfoProcessor.process(courtDataDTO);
        log.info("Create Link - WQ Core Info is Processed");
        wqLinkRegisterProcessor.process(courtDataDTO);
        log.info("Create Link - WQ Link Details are Processed");
        solicitorInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Solicitor Details are Processed");
        proceedingsInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Proceeding Details are Processed");
        defendantInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Defendant Details are Processed");
        sessionInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Session Details are Processed");
        offenceInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Offence Details are Processed");
        resultsInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Results Details are Processed");
        repOrderInfoProcessor.process(courtDataDTO);
        log.info("Create Link - Rep Order Details are Processed");
        log.info("Create Link - Transaction Processing - End");
    }

    private void mapIdentifiers(CourtDataDTO courtDataDTO) {
        courtDataDTO.setTxId(identifierRepository.getTxnID());
        courtDataDTO.setLibraId(identifierRepository.getLibraID());
        courtDataDTO.setProceedingId(identifierRepository.getProceedingID());
        courtDataDTO.setCaseId(identifierRepository.getCaseID());
    }

}
