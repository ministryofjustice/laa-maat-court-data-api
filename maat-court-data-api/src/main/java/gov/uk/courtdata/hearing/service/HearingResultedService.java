package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedService {

    private final HearingValidationProcessor hearingValidationProcessor;
    private  final HearingResultedImpl hearingResultedImpl;

    public void process(final CaseDetails caseDetails) {

        CourtDataDTO courtDataDTO = hearingValidationProcessor.validate(caseDetails);

        hearingResultedImpl.execute(courtDataDTO);
    }
}
