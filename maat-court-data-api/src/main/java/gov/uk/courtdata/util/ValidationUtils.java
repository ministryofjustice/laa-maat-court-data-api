package gov.uk.courtdata.util;

import gov.uk.courtdata.exception.ValidationException;
import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class ValidationUtils {
    public static <T> void isEmpty(final Collection<T> collection, final String errorMessage) {
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
        for (final T element : collection) {
            if (element == null) {
                return true;
            }
        }
        return false;
    }

    private static void raise(final String errorMessage) {
        throw new ValidationException(errorMessage);
    }
}