package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateContributionsValidatorTest {

    @InjectMocks
    private UpdateContributionsValidator updateContributionsValidator;

    @Mock
    private ContributionsRepository contributionsRepository;

    @Test
    public void givenValidContributionsData_whenValidateIsInvoked_thenValidationPasses() {
        Integer testId = 999;
        UpdateContributions updateContributions = UpdateContributions.builder().id(testId).build();
        when(updateContributionsValidator.validateTransferStatus(any())).thenReturn(Optional.empty());
        when(contributionsRepository.existsById(any())).thenReturn(true);

        Optional<Void> result = updateContributionsValidator.validate(updateContributions);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void givenNoIdInContributionsData_whenValidateIsInvoked_thenValidationFails() {
        UpdateContributions updateContributions = UpdateContributions.builder().id(null).build();

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> updateContributionsValidator.validate(updateContributions))
                .withMessageContaining("Contributions ID is required.");
    }

    @Test
    public void givenInvalidIdInContributionsData_whenValidateIsInvoked_thenValidationFails() {
        Integer testId = 666;
        UpdateContributions updateContributions = UpdateContributions.builder().id(testId).build();
        when(contributionsRepository.existsById(any())).thenReturn(false);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> updateContributionsValidator.validate(updateContributions))
                .withMessageContaining(String.format("Contributions ID: %d is invalid.", testId));
    }
}
