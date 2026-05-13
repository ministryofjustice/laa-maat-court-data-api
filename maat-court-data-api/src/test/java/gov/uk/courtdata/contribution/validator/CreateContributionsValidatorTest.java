package gov.uk.courtdata.contribution.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.MaatIdValidator;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;
import uk.gov.justice.laa.crime.enums.contribution.TransferStatus;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Disabled
class CreateContributionsValidatorTest {

    private static final Integer TEST_REP_ID = 999;

    @InjectMocks
    private CreateContributionsValidator createContributionsValidator;

    @Mock
    private MaatIdValidator maatIdValidator;

    @Test
    void givenAValidId_whenValidateIsInvoked_thenValidationPasses() {
        CreateContributionRequest createContributions =
                new CreateContributionRequest().withRepId(TEST_REP_ID).withTransferStatus(TransferStatus.RECEIVED);
        when(maatIdValidator.validate(anyInt())).thenReturn(Optional.empty());

        Optional<Void> result = createContributionsValidator.validate(createContributions);

        assertThat(result).isNotPresent();
    }

    @Test
    void givenInvalidId_whenValidateIsInvoked_thenValidationFails() {
        CreateContributionRequest createContributions =
                new CreateContributionRequest().withRepId(666).withTransferStatus(TransferStatus.RECEIVED);
        when(maatIdValidator.validate(anyInt())).thenThrow(ValidationException.class);

        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> createContributionsValidator.validate(createContributions));
    }
}
