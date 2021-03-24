package gov.uk.courtdata.enums;

public enum ModeOfTrial {

    NO_MODE_OF_TRAIL(7);

    private final int value;

    ModeOfTrial(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    }
