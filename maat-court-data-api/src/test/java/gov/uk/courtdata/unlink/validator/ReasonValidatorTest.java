package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReasonValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    private ReasonValidator reasonValidator;

    @Test
    public void testReasonIdValidator_whenReasonIDIsNullThrowsException() {

        thrown.expect(ValidationException.class);
        thrown.expectMessage("Reason id is missing.");
        reasonValidator.validate(null);
    }
}
