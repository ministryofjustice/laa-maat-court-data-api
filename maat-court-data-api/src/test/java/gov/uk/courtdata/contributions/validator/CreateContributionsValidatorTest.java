package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateContributionsValidatorTest {

    @InjectMocks
    private CreateContributionsValidator createContributionsValidator;

    @Mock
    private MaatIdValidator maatIdValidator;

    @Test
    public void givenValidContributionsData_whenValidateIsInvoked_thenValidationPasses() {
        CreateContributions createContributions = CreateContributions.builder().build();
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        when(createContributionsValidator.validateTransferStatus(any())).thenReturn(Optional.empty());

        Optional<Void> result = createContributionsValidator.validate(createContributions);

        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void givenInvalidContributionsData_whenValidateIsInvoked_thenValidationFails() {
        CreateContributions createContributions = CreateContributions.builder().build();
        when(maatIdValidator.validate(any())).thenThrow(ValidationException.class);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> createContributionsValidator.validate(createContributions));
    }
}
