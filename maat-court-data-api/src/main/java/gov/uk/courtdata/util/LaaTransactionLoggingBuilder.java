package gov.uk.courtdata.util;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;

public class LaaTransactionLoggingBuilder {

    private static LaaTransactionLogging getLaaTransactionLogging(CaseDetails linkMessage, RuntimeException ex) {
        return LaaTransactionLogging.builder().maatId(linkMessage.getMaatId())
                .laaTransactionId(linkMessage.getLaaTransactionId())
                .caseUrn(linkMessage.getCaseUrn())
                .message(ex.getMessage())
                .build();
    }

}
