package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserIdValidatorTest {

    @InjectMocks
    private UserIdValidator userIdValidator;


    @Test
    public void testUserIdValidator_whenUserIsNullThrowsException() {

        Assertions.assertThrows(ValidationException.class, () ->
                userIdValidator.validate(null), "User id is missing.");

    }

    @Test
    public void testUserIdValidator_whenUserIsEmpTyUserIDThrowsException() {

        Assertions.assertThrows(ValidationException.class,
                () -> userIdValidator.validate(""), "User id is missing.");

    }
}
