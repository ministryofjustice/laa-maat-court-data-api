package gov.uk.courtdata.enums;

import java.util.HashMap;
import java.util.Map;

public enum ApplicationClassification {


    CO("CO", "CO Criminal Related"),
    CR("CR", "CR CR"),
    EF("EF", "EF Fines"),
    EM("EM", "EF Fines"),
    CE("CE", "CE Either Way"),
    CB("CB", "CB Breaches"),
    CS("CS", "CS Summary Non-Motoring"),
    CC("CC", "CC Crown Court"),
    VA("VA", "VA Misc. Applications");


    private final String code;
    private final String description;

    ApplicationClassification(String code, String description) {
        this.code = code;
        this.description = description;

    }

    private static final HashMap<String,String> MAP = new HashMap<String,String>();

    static {
        for (ApplicationClassification s : ApplicationClassification.values()) {
            MAP.put(s.code, s.description);
        }
    }

    public String getCode() {
        return code;
    }


    public static String getDescriptionByCode(String code) {
        return MAP.get(code);
    }

}
