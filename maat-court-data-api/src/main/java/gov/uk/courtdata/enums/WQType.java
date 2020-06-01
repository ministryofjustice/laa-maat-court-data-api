package gov.uk.courtdata.enums;

/**
 * <Code>WQType</Code> list of work queue types.
 */
public enum WQType {
    COMMITTAL_QUEUE(1),
    INDICTABLE_QUEUE(2),
    CONCLUSION_QUEUE(7),
    PRE_STEERING(17),
    USER_INTERVENTIONS_QUEUE(8);

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
