package gov.uk.courtdata.util;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;
import gov.uk.courtdata.model.hearing.HearingResulted;


public class LaaTransactionLoggingBuilder {

    private LaaTransactionLoggingBuilder() {

    }

    public static LaaTransactionLogging get(CaseDetails caseDetails) {
        Gson gson = new Gson();
        return get(gson.toJson(caseDetails));
    }

    public static LaaTransactionLogging get(HearingResulted hearingResulted) {
        Gson gson = new Gson();
        return get(gson.toJson(hearingResulted));
    }



    public static LaaTransactionLogging get(String message) {
        Gson gson = new Gson();
        return gson.fromJson(message, LaaTransactionLogging.class);

    }

    public static String getStr(String message) {
        return get(message).toString();
    }


}
