package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserIdValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    private UserIdValidator userIdValidator;



    @Test
    public void testUserIdValidator_whenUserIsNullThrowsException() {

        thrown.expect(ValidationException.class);
        thrown.expectMessage("User id is missing.");
        userIdValidator.validate(null);
    }
}
