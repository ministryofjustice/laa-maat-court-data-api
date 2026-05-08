package gov.uk.courtdata.validator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LinkRegisterValidatorTest {

    @InjectMocks
    private LinkRegisterValidator linkRegisterValidator;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Test
    public void givenZeroLinks_whenValidatorsIsInvoked_thenValidationIsFailed() {
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> linkRegisterValidator.validate(10));
        assertThat(validationException.getMessage()).isEqualTo("MAAT Id : " + 10 + " not linked.");
    }

    @Test
    public void givenMoreThanOneLinksAvailable_whenValidatorsIsInvoked_thenValidationIsFailed() {
        when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt())).thenReturn(2);
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> linkRegisterValidator.validate(10));
        assertThat(validationException.getMessage()).isEqualTo("Multiple Links found for  MAAT Id : " + 10);
    }
}
