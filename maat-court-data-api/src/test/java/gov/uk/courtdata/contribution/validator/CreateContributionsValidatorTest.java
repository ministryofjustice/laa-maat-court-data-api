package gov.uk.courtdata.contribution.validator;

import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateContributionsValidatorTest {

    private static final Integer TEST_REP_ID = 999;
    private static final String TEST_TRANSFER_STATUS = "RECEIVED";

    @InjectMocks
    private CreateContributionsValidator createContributionsValidator;
    @Mock
    private MaatIdValidator maatIdValidator;

    @Test
    void givenAValidId_whenValidateIsInvoked_thenValidationPasses() {
        CreateContributions createContributions = CreateContributions.builder().repId(TEST_REP_ID)
                .transferStatus(TEST_TRANSFER_STATUS).build();
        when(maatIdValidator.validate(anyInt())).thenReturn(Optional.empty());

        Optional<Void> result = createContributionsValidator.validate(createContributions);

        assertThat(result).isNotPresent();
    }

    @Test
    void givenInvalidId_whenValidateIsInvoked_thenValidationFails() {
        CreateContributions createContributions = CreateContributions.builder().repId(666)
                .transferStatus(TEST_TRANSFER_STATUS).build();
        when(maatIdValidator.validate(anyInt())).thenThrow(ValidationException.class);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> createContributionsValidator.validate(createContributions));
    }

    @Test
    void givenInvalidTransferStatus_whenValidateIsInvoked_thenValidationFails() {
        CreateContributions createContributions = CreateContributions.builder().repId(TEST_REP_ID)
                .transferStatus("INVALID").build();
        when(maatIdValidator.validate(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> createContributionsValidator.validate(createContributions));
    }
}
