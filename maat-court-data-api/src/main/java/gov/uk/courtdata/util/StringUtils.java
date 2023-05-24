package gov.uk.courtdata.util;

public final class StringUtils {
    public static String applyMaxLengthLimitToString(String stringToTruncate, int maxLength) {
        return stringToTruncate != null && stringToTruncate.length() > maxLength ?
                stringToTruncate.substring(0, maxLength) : stringToTruncate;
    }
}

