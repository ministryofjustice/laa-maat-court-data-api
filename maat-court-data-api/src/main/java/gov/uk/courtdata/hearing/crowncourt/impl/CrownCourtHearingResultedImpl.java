package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.hearing.processor.HearingWQProcessor;
import gov.uk.courtdata.hearing.processor.WQCoreProcessor;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.ACTIONABLE_WQ_QUEUES;

@Component
public class CrownCourtHearingResultedImpl extends HearingResultedImpl {

    private WQCoreProcessor wqCoreProcessor;

    public CrownCourtHearingResultedImpl(HearingDTOMapper hearingDTOMapper, IdentifierRepository identifierRepository, WqLinkRegisterRepository wqLinkRegisterRepository, HearingWQProcessor hearingWQProcessor, ResultCodeRefDataProcessor resultCodeRefDataProcessor, OffenceCodeRefDataProcessor offenceCodeRefDataProcessor) {
        super(hearingDTOMapper, identifierRepository, wqLinkRegisterRepository, hearingWQProcessor, resultCodeRefDataProcessor, offenceCodeRefDataProcessor);
    }


    @Override
    protected boolean isWorkQueueProcessingRequired(Integer resultCode) {
       return !ACTIONABLE_WQ_QUEUES.contains(wqCoreProcessor.findWQType(resultCode));

    }

    @Autowired
    public void setWqCoreProcessor(WQCoreProcessor wqCoreProcessor) {
        this.wqCoreProcessor = wqCoreProcessor;
    }
}
