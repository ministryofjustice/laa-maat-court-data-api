package gov.uk.courtdata.prosecutionconcluded.request;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.validator.ProsecutionConcludedValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProsecutionConcludedValidatorTest {

    @InjectMocks
    private ProsecutionConcludedValidator prosecutionConcludedValidator;

    @Test
    public void testWhenProsecutionConcludedRequestIsNull_thenThrowException() {
        Assertions.assertThrows(ValidationException.class, () ->
                prosecutionConcludedValidator.validateRequestObject(null));
    }

    @Test
    public void testWhenProsecutionConcludedListIsEmpty_thenThrowException() {

        ProsecutionConcluded request = ProsecutionConcluded.builder().build();
        Assertions.assertThrows(ValidationException.class, () ->
                prosecutionConcludedValidator.validateRequestObject(request));
    }

    @Test
    public void testWhenProsecutionConcludedListIsNull_thenThrowException() {

        ProsecutionConcluded request = ProsecutionConcluded.builder().offenceSummary(null)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> {
            prosecutionConcludedValidator.validateRequestObject(request);
        });


        assertThat(request);
    }

    @Test
    public void testWhenOuCodeIsNull_thenThrowException() {
        Assertions.assertThrows(ValidationException.class, () -> {
            prosecutionConcludedValidator.validateOuCode(null);
        });
    }

    @Test
    public void testWhenOuCodeIsEmpty_thenThrowException() {
        Assertions.assertThrows(ValidationException.class, () -> prosecutionConcludedValidator.validateOuCode(""));
    }
}