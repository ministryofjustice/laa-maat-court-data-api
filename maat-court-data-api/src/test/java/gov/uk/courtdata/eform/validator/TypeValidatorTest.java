package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeValidatorTest {

    private TypeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TypeValidator();
    }

    @Test
    void shouldThrowValidationExceptionWhenValidatingANullType() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> validator.validate(null));

        assertEquals("Expected non-null, non-empty, non-blank type but found [null]", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenValidatingAnEmptyType() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> validator.validate(StringUtils.EMPTY));

        assertEquals("Expected non-null, non-empty, non-blank type but found []", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenValidatingABlankType() {
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class, () -> validator.validate("      "));

        assertEquals("Expected non-null, non-empty, non-blank type but found [      ]", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationExceptionWhenValidatingAValidType() {
        validator.validate("CRM14");
    }
}