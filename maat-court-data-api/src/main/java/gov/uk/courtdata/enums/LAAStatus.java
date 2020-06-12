package gov.uk.courtdata.enums;

public enum LAAStatus {

    GRANTED("GR"),
    GRANTED_FOR_TWO_ADVOCATES("G2"),
    GRANTED_WITH_QC("GQ"),
    FAILED_ON_IOJ("FJ"),
    FAILED_BOTH_IOJ_AND_MEANS("FB");


    private String value;

    LAAStatus(final String value) {
        this.value = value;
    }

    public static boolean isGrantedLAAStatus(String laaStatus) {
        boolean isGranted = false;
        if (GRANTED.value.equalsIgnoreCase(laaStatus)
                || GRANTED_FOR_TWO_ADVOCATES.value.equalsIgnoreCase(laaStatus)
                || GRANTED_WITH_QC.value.equalsIgnoreCase(laaStatus)) {
            isGranted = true;
        }
        return isGranted;
    }

    public static boolean isFailedLAAStatus(String laaStatus) {
        boolean isGranted = false;
        if (FAILED_ON_IOJ.value.equalsIgnoreCase(laaStatus)
                || FAILED_BOTH_IOJ_AND_MEANS.value.equalsIgnoreCase(laaStatus)) {
            isGranted = true;
        }
        return isGranted;
    }
}
