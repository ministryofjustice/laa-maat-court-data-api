package gov.uk.courtdata.converter;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public final class YesNoConvertor {

    private static final String STRING_Y = "Y";
    private static final String STRING_N = "N";

    public static String toString(Boolean attribute) {
        if (Boolean.TRUE.equals(attribute)) {
            return STRING_Y;
        } else if (Boolean.FALSE.equals(attribute)) {
            return STRING_N;
        }

        return null;
    }

    public static Optional<Boolean> toBoolean(String attribute) {
        if (attribute == null) {
            return Optional.empty();
        }

        return switch (attribute) {
            case STRING_Y -> Optional.of(true);
            case STRING_N -> Optional.of(false);
            default -> throw new IllegalArgumentException("Unknown boolean value: " + attribute);
        };
    }
}
