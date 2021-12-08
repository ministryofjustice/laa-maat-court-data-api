package gov.uk.courtdata.hearing.processor;


import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class HearingWQProcessor {

    private final WQCaseProcessor wqCaseProcessor;

    private final WQCoreProcessor wqCoreProcessor;

    private final WQDefendantProcessor wqDefendantProcessor;

    private final WQOffenceProcessor wqOffenceProcessor;

    private final WQResultProcessor wqResultProcessor;

    private final WQSessionProcessor wqSessionProcessor;


    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void process(final HearingDTO hearingDTO) {

        log.info("Create WQ case");
        wqCaseProcessor.process(hearingDTO);
        log.info("Create WQ session");
        wqSessionProcessor.process(hearingDTO);
        log.info("Create WQ defendant");
        wqDefendantProcessor.process(hearingDTO);
        log.info("Create WQ offence");
        wqOffenceProcessor.process(hearingDTO);
        log.info("Create WQ result");
        wqResultProcessor.process(hearingDTO);
        log.info("Create WQ core");
        wqCoreProcessor.process(hearingDTO);

    }


}