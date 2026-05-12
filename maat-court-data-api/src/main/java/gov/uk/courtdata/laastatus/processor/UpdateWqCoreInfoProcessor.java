package gov.uk.courtdata.laastatus.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_UPDATE_CASE_EVENT;

import gov.uk.courtdata.link.processor.WqCoreInfoProcessor;
import gov.uk.courtdata.repository.WqCoreRepository;

import org.springframework.stereotype.Component;

@Component
public class UpdateWqCoreInfoProcessor extends WqCoreInfoProcessor {

    public UpdateWqCoreInfoProcessor(WqCoreRepository wqCoreRepository) {
        super(wqCoreRepository);
    }

    @Override
    protected Integer getWQEvent() {
        return WQ_UPDATE_CASE_EVENT;
    }
}
