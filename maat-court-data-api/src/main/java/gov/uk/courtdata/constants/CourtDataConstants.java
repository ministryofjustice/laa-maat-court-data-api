package gov.uk.courtdata.constants;

import java.util.List;

public final class CourtDataConstants {

    private CourtDataConstants() {

    }

    public static final Integer WQ_CREATION_EVENT = 20;
    public static final Integer WQ_UPDATE_CASE_EVENT = 22;
    public static final Integer WQ_SUCCESS_STATUS = 0;
    public static final String SEARCH_TYPE_0 = "0";
    public static final String CREATE_LINK = "createlink";
    public static final String SAVE_MLR = "savemlr";
    public static final Integer PENDING_IOJ_DECISION = 2;
    public static final Integer G_NO = 0;
    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String DEFAULT_HEARING_CUS_STATUS = "D";
    public static final String COMMON_PLATFORM = "CP";
    public static final Integer WQ_UNLINK_EVENT = 24;
    public static final String LEADING_ZERO_3 = "%03d";
    public static final String LEADING_ZERO_2 = "%02d";


    public static final String MAGS_PROCESSING_SYSTEM_USER = "System-CP";
    public static final String AUTO_USER = "AUTO";
    public static final String RESULT_CODE_DESCRIPTION = "Automatically added result";
    public static final String UNKNOWN_OFFENCE = "UNKNOWN OFFENCE";

    public static final String SYSTEM_UNLINKED = "System Unlinked";
    public static final String RESULT_CODE_1000 = "1000";
    public static final Integer APPLICATION_ASN_SEQ_INITIAL_VALUE = 1000;

    public static final String RESERVATION_RECORD_NAME = "REP_ORDER";

    public static final List<String> RESERVATION_SPECIAL_USERNAMES = List.of("HUB", "MLA");
    public static final Integer COMMITTAL_FOR_TRIAL_SUB_TYPE = 2;
    public static final Integer COMMITTAL_FOR_SENTENCE_SUB_TYPE = 1;
    public static final String LAA_TRANSACTION_ID = "Laa-Transaction-Id";
    public static final String CDA_TRANSACTION_ID_HEADER = "X-Request-ID";
    public static final int ORACLE_VARCHAR_MAX = 4000;

}
