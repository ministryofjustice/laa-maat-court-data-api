package gov.uk.courtdata.enums;

/**
 * <Code>WQType</Code> list of work queue types.
 */
public enum WQType {

    PRE_STEERING(17);

    private int value;

    /**
     *
     * @param value
     */
    WQType(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
