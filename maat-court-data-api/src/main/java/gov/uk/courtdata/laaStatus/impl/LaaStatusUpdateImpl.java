package gov.uk.courtdata.laaStatus.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.processor.UpdateDefendantInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqCoreInfoProcessor;
import gov.uk.courtdata.link.processor.*;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
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
    public void execute(CourtDataDTO courtDataDTO) {

        log.debug("Table Update Transaction Process - Start");
        mapTxnID(courtDataDTO);
        caseInfoProcessor.process(courtDataDTO);
        updateWqCoreInfoProcessor.process(courtDataDTO);
        wqLinkRegisterProcessor.process(courtDataDTO);
        solicitorInfoProcessor.process(courtDataDTO);
        proceedingsInfoProcessor.process(courtDataDTO);
        updateDefendantInfoProcessor.process(courtDataDTO);
        sessionInfoProcessor.process(courtDataDTO);
        offenceInfoProcessor.process(courtDataDTO);
        log.debug("Table Update Transaction Process - End");
    }

    private void mapTxnID(CourtDataDTO courtDataDTO) {

        courtDataDTO.setTxId(identifierRepository.getTxnID());

    }
}
