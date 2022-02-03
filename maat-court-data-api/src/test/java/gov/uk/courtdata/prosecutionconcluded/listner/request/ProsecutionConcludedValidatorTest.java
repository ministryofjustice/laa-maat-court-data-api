package gov.uk.courtdata.prosecutionconcluded.listner.request;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ProsecutionConcludedValidatorTest {

    @InjectMocks
    private ProsecutionConcludedValidator prosecutionConcludedValidator;

    @Test (expected = ValidationException.class)
    public void testWhenProsecutionConcludedRequestIsNull_thenThrowException() {
        prosecutionConcludedValidator.validateRequestObject(null);
    }
    @Test (expected = ValidationException.class)
    public void testWhenProsecutionConcludedListIsEmpty_thenThrowException() {

        ProsecutionConcluded request = ProsecutionConcluded.builder().build();
        prosecutionConcludedValidator.validateRequestObject(request);
    }

    @Test (expected = ValidationException.class)
    public void testWhenProsecutionConcludedListIsNull_thenThrowException() {

        ProsecutionConcluded request = ProsecutionConcluded.builder().offenceSummary(null)
                .build();

        prosecutionConcludedValidator.validateRequestObject(request);

        assertThat(request);
    }

    @Test (expected = ValidationException.class)
    public void testWhenOuCodeIsNull_thenThrowException() {
        prosecutionConcludedValidator.validateOuCode(null);
    }
    @Test (expected = ValidationException.class)
    public void testWhenOuCodeIsEmpty_thenThrowException() {
        prosecutionConcludedValidator.validateOuCode("");
    }
}