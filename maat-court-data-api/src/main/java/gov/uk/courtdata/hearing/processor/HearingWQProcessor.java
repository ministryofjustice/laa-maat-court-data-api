package gov.uk.courtdata.hearing.processor;


import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import static gov.uk.courtdata.enums.JurisdictionType.CROWN;

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

    private final WQHearingProcessor wqHearingProcessor;

    private final PleaProcessor pleaProcessor;

    private final VerdictProcessor verdictProcessor;

    private final LinkRegisterProcessor linkRegisterProcessor;

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

        if (hearingDTO.getJurisdictionType().equals(CROWN)) {
            processCCOutComeData(hearingDTO);
        }
    }

    private void processCCOutComeData(HearingDTO hearingDTO) {
        log.info("Calling plea processor ");
        pleaProcessor.process(hearingDTO);
        log.info("Calling verdict processor");
        verdictProcessor.process(hearingDTO);
        log.info("Calling linkRegisterProcessor processor");
        linkRegisterProcessor.process(hearingDTO);
    }
}