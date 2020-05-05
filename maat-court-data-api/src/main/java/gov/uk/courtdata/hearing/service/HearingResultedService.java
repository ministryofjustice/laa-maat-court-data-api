package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedService {

    private final HearingValidationProcessor hearingValidationProcessor;
    private final HearingResultedImpl hearingResultedImpl;

    public void process(final HearingDetails hearingDetails) {

        hearingValidationProcessor.validate(hearingDetails);
        log.info("Validation Completed successfully for MAAT ID: {0},and LaaTransactionId: {1}",
                hearingDetails.getMaatId(),hearingDetails.getLaaTransactionId());
        hearingResultedImpl.execute(hearingDetails);
    }
}
