package gov.uk.courtdata.util;

import gov.uk.courtdata.exception.ValidationException;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;

@UtilityClass
public class ValidationUtils {
    public static <T> void isEmptyOrHasNullElement(final Collection<T> collection, final String errorMessage) {
        if (collection == null || collection.isEmpty() || containsAnyNull(collection)) {
            raise(errorMessage);
        }
    }

    public static void isNull(final Object o, final String errorMessage) {
        if (o == null) {
            raise(errorMessage);
        }
    }

    private static <T> boolean containsAnyNull(final Collection<T> collection) {
        return collection.stream().anyMatch(Objects::isNull);
    }

    private static void raise(final String errorMessage) {
        throw new ValidationException(errorMessage);
    }
}