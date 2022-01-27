package gov.uk.courtdata.prosecutionconcluded.listner.request;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcludedRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;
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

        ProsecutionConcludedRequest request = ProsecutionConcludedRequest.builder()
                .prosecutionConcludedList(Collections.emptyList())
                .build();
        prosecutionConcludedValidator.validateRequestObject(request);
    }

    @Test (expected = ValidationException.class)
    public void testWhenProsecutionConcludedListIsNull_thenThrowException() {

        ProsecutionConcludedRequest request = ProsecutionConcludedRequest.builder()
                .prosecutionConcludedList(null)
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