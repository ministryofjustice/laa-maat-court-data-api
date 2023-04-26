package gov.uk.courtdata.contributions.validator;


import gov.uk.courtdata.exception.ValidationException;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;


public class ContributionsValidatorTest {

    private ContributionsValidator contributionsValidator;

    @Test
    public void givenValidTransferStatus_whenValidateTransferStatusIsInvoked_thenValidationPasses() {
        String testTransferStatus = "RECEIVED";

        Optional<Void> result = contributionsValidator.validateTransferStatus(testTransferStatus);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void givenInvalidTransferStatus_whenValidateTransferStatusIsInvoked_thenValidationFails() {
        String testTransferStatus = "INVALID";

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> contributionsValidator.validateTransferStatus(testTransferStatus))
                .withMessageContaining(String.format("Transfer Status: %d is invalid.", testTransferStatus));


    }
}
