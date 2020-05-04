package gov.uk.courtdata.hearing.magistrate.impl;


import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.hearing.processor.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MagistrateCourtImpl {


    private final WQCaseProcessor wqCaseProcessor;

    private final WQCoreProcessor wqCoreProcessor;

    private final WQDefendantProcessor wqDefendantProcessor;

    private final WQOffenceProcessor wqOffenceProcessor;

    private final WQResultProcessor wqResultProcessor;

    private final WQSessionProcessor wqSessionProcessor;

    @Transactional(rollbackFor = MaatCourtDataException.class)
    public void execute(final MagistrateCourtDTO magistrateCourtDTO) {

        log.info("create wq case");
        wqCaseProcessor.process(magistrateCourtDTO);
        log.info("create wq session");
        wqSessionProcessor.process(magistrateCourtDTO);
        log.info("create wq defendant");
        wqDefendantProcessor.process(magistrateCourtDTO);
        log.info("create wq offence");
        wqOffenceProcessor.process(magistrateCourtDTO);
        log.info("create wq result");
        wqResultProcessor.process(magistrateCourtDTO);
        log.info("create wq core ");
        wqCoreProcessor.process(magistrateCourtDTO);
        log.info("WQ tables created.");
    }
}
