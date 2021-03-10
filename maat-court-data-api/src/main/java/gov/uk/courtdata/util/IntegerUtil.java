package gov.uk.courtdata.util;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class IntegerUtil {

    public static Optional<Integer> parse(String strValue) throws NumberFormatException {

        if (strValue == null || strValue.length() < 0)
            return Optional.empty();

        strValue = strValue.trim();
        if ("".equals(strValue))
            return Optional.empty();

        return Optional.ofNullable(strValue)
                .map(Integer::parseInt);
    }
}
