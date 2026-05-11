package gov.uk.courtdata.link.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkExistsValidatorTest {

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @InjectMocks
    private LinkExistsValidator linkExistsValidator;

    @Test
    void testWhenLinkNotExists_validationPasses() {

        Integer testId = 1000;
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(testId)).thenReturn(BigDecimal.ZERO.intValue());
        Optional result = linkExistsValidator.validate(testId);
        assertThat(result).isNotPresent();
    }

    @Test
    void testWhenLinkAlreadyExists_throwsException() {
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt()))
                .thenReturn(BigDecimal.ONE.intValue());
        assertThatThrownBy(() -> linkExistsValidator.validate(Mockito.anyInt()))
                .isInstanceOf(ValidationException.class)
                .hasMessage("0 is already linked to a case.");
    }
}
