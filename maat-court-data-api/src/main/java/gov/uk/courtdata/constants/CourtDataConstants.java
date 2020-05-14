package gov.uk.courtdata.constants;

import java.util.Arrays;
import java.util.List;

public interface CourtDataConstants {

    Integer WQ_CREATION_EVENT = 20;
    Integer WQ_UPDATE_CASE_EVENT = 22;
    Integer WQ_SUCCESS_STATUS = 0;
    String SEARCH_TYPE_0 = "0";
    String CREATE_LINK = "createlink";
    String SAVE_MLR = "savemlr";
    Integer PENDING_IOJ_DECISION = 2;
    Integer G_NO = 0;
    String YES = "Y";
    String NO = "N";
    String DEFAULT_HEARING_CUS_STATUS = "D";
    String COMMON_PLATFORM = "CP";
    Integer WQ_UNLINK_EVENT = 24;
    String LEADING_ZERO_3 ="%03d";

    List<Integer> FAIL_IOJ_DECISION = Arrays.asList(0, 2);
    int PASS_IOJ_DECISION = 1;
    int NA_IOJ_DECISION = 3;

    List<String> GRANTED_LAA_STATUS = Arrays.asList("GR", "G2", "GQ");
    List<String> FAILED_LAA_STATUS = Arrays.asList("FJ", "FB");
    String CROWN_COURT = "CROWN";
    String MAGS_PROCESSING_SYSTEM_USER = "System-CP";
    String AUTO_USER = "AUTO";
    String RESULT_CODE_DESCRIPTION = "Automatically added result";
    String UNKNOWN_OFFENCE = "UNKNOWN OFFENCE";
}
