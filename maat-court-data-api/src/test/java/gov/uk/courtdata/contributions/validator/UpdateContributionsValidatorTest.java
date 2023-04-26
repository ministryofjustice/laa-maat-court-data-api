package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateContributionsValidatorTest {

    private static final Integer TEST_ID = 999;
    private static final String TEST_TRANSFER_STATUS = "RECEIVED";

    @InjectMocks
    private UpdateContributionsValidator updateContributionsValidator;
    @Mock
    private ContributionsRepository contributionsRepository;

    @Test
    void whenValidateIsInvoked_thenValidationPasses() {
        UpdateContributions updateContributions = UpdateContributions.builder().id(TEST_ID)
                .transferStatus(TEST_TRANSFER_STATUS).build();
        when(contributionsRepository.existsById(anyInt())).thenReturn(true);

        Optional<Void> result = updateContributionsValidator.validate(updateContributions);

        assertThat(result).isNotPresent();
    }

    @Test
    void givenNoId_whenValidateIsInvoked_thenValidationFails() {
        UpdateContributions updateContributions = UpdateContributions.builder().id(null)
                .transferStatus(TEST_TRANSFER_STATUS).build();

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> updateContributionsValidator.validate(updateContributions))
                .withMessageContaining("Contributions ID is required.");
    }

    @Test
    void givenInvalidId_whenValidateIsInvoked_thenValidationFails() {
        UpdateContributions updateContributions = UpdateContributions.builder().id(TEST_ID)
                .transferStatus(TEST_TRANSFER_STATUS).build();
        when(contributionsRepository.existsById(anyInt())).thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> updateContributionsValidator.validate(updateContributions))
                .withMessageContaining(String.format("Contributions ID: %d is invalid.", TEST_ID));
    }

    @Test
    void givenInvalidTransferStatus_whenValidateIsInvoked_thenValidationFails() {
        UpdateContributions updateContributions = UpdateContributions.builder().id(TEST_ID)
                .transferStatus("INVALID").build();
        when(contributionsRepository.existsById(anyInt())).thenReturn(true);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> updateContributionsValidator.validate(updateContributions))
                .withMessageContaining("Transfer Status: INVALID is invalid.", "INVALID");
    }
}
