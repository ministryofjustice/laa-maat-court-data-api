package gov.uk.courtdata.enums;

/**
 * <code>WQStatus</code> is list of work queue statuses.
 */
public enum WQStatus {

    SUCCESS(0),
    FAIL(1),
    WAITING(2),
    WARNING(3);

    private int value;

    /**
     * @param value
     */
    WQStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
