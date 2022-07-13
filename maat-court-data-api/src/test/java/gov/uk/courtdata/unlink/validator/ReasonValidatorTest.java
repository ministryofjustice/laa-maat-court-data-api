package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReasonValidatorTest {


    @InjectMocks
    private ReasonValidator reasonValidator;

    @Test
    public void testReasonIdValidator_whenReasonIDIsNullThrowsException() {

        Assertions.assertThrows(ValidationException.class,()->{
            reasonValidator.validate(null);
        },"Reason id is missing.");

    }

    @Test
    public void testReasonIdValidator_whenReasonIDIsZeroThrowsException() {

        Assertions.assertThrows(ValidationException.class,()->{
            reasonValidator.validate(0);
        }, "Reason id is missing.");

    }
}
