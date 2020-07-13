package gov.uk.courtdata.util;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;


public class LaaTransactionLoggingBuilder {

    public static LaaTransactionLogging get(CaseDetails caseDetails) {
        Gson gson = new Gson();
        return get(gson.toJson(caseDetails));
    }

    public static LaaTransactionLogging get(String message) {
        Gson gson = new Gson();
        return gson.fromJson(message, LaaTransactionLogging.class);

    }

    public static String getStr(String message) {
        return get(message).toString();
    }
}
