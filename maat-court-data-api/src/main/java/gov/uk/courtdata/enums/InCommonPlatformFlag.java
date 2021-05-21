package gov.uk.courtdata.enums;

public enum InCommonPlatformFlag {

    YES("Y"),
    NO("N");

    private String value;

    InCommonPlatformFlag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}