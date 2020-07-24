package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.enums.CCTrialOutcome.isConvicted;

@AllArgsConstructor
@Component
public final class CrownCourtProcessHelper {

    private final XLATResultRepository xlatResultRepository;


    public String isImprisoned(final HearingResulted hearingResulted, final String ccOutcome) {

        if (isConvicted(ccOutcome)) {

            List<String> offenceResultCodes = flattenResults(hearingResulted);
            return anyResultCodeMatch(imprisonmentResultCodes(), offenceResultCodes) ? YES : NO;
        }
        return null;
    }

    public String isBenchWarrantIssued(final HearingResulted hearingResulted) {

        List<String> offenceResultCodes = flattenResults(hearingResulted);
        return anyResultCodeMatch(xlatResultRepository.findByCjsResultCodeIn(), offenceResultCodes) ? YES : NO;
    }

    private boolean anyResultCodeMatch(final List<XLATResultEntity> resultCodes, final List<String> resultsFlattened) {
        return resultCodes
                .stream()
                .map(XLATResultEntity::getCjsResultCode)
                .map(String::valueOf)
                .anyMatch(resultsFlattened::contains);
    }

    private List<String> flattenResults(final HearingResulted hearingResulted) {
        return hearingResulted
                .getDefendant()
                .getOffences()
                .stream()
                .flatMap(offence -> offence.getResults().stream())
                .map(Result::getResultCode)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<XLATResultEntity> imprisonmentResultCodes() {
        return xlatResultRepository.fetchResultCodesForCCImprisonment();
    }


}
