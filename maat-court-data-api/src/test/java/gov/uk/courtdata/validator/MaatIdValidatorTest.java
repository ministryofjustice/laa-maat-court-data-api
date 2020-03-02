package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MaatIdValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Spy
    private MaatIdValidator maatIdValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenMaatIdIsNull_throwsException() {
        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");
        maatIdValidator.validate(null);
    }


    @Test
    public void testWhenMaatIsNotNull_validationPasses() {
        Optional result = maatIdValidator.validate(1000);
    }
}
