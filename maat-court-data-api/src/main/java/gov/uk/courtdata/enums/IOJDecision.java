package gov.uk.courtdata.enums;

public enum IOJDecision {

    FAIL(0),
    PASS(1),
    PENDING(2),
    NOT_APPLICABLE(3);

    private int value;

    IOJDecision(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static boolean isFailedDecision(int decision) {
        boolean myReturn = false;
        if (FAIL.value == decision
                || PENDING.value == decision) {
            myReturn = true;
        }
        return myReturn;
    }
}
