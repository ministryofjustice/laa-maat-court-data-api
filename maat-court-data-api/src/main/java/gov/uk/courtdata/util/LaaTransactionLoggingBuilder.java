package gov.uk.courtdata.util;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;

import java.util.Optional;

public class LaaTransactionLoggingBuilder {

    public static LaaTransactionLogging get(CaseDetails caseDetails) {
        return LaaTransactionLogging.builder()
                .maatId(caseDetails.getMaatId())
                .caseUrn(caseDetails.getCaseUrn())
                .laaTransactionId(Optional.ofNullable(caseDetails.getLaaTransactionId()).orElse(null))
                .build();
    }

    public static String getStr(CaseDetails caseDetails) {
        return get(caseDetails).toString();
    }

}
