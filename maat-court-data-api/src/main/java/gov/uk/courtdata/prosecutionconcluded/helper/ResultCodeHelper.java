package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isConvicted;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isTrial;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultCodeHelper {

    private final XLATResultRepository xlatResultRepository;

    public String isBenchWarrantIssued(final String caseLevelOutcome, List<String> hearingResultCodes) {

        if (isTrial(caseLevelOutcome)) {

            return anyResultCodeMatch(xlatResultRepository.findByCjsResultCodeIn(), hearingResultCodes) ? YES : null;
        }

        return null;
    }

    public String isImprisoned(final String caseLevelOutcome, List<String> hearingResultCodes) {

        if (isConvicted(caseLevelOutcome)) {

            return anyResultCodeMatch(xlatResultRepository.fetchResultCodesForCCImprisonment(), hearingResultCodes) ? YES : NO;
        }
        return null;
    }

    private boolean anyResultCodeMatch(final List<XLATResultEntity> resultCodes, final List<String> hearingResultCodes) {
        return resultCodes
                .stream()
                .map(XLATResultEntity::getCjsResultCode)
                .map(String::valueOf)
                .anyMatch(hearingResultCodes::contains);
    }
}