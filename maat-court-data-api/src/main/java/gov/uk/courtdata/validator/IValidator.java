package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;

import java.util.Optional;

/**
 * Interface for validator.
 *
 * @param <V>  object to validate.
 * @param <T> return type.
 */
public interface IValidator<T, V> {


    /**
     * Validate the passed in json.
     *
     * @param value
     * @return
     * @throws ValidationException
     */
    Optional<T> validate(final V value) throws ValidationException;

}
