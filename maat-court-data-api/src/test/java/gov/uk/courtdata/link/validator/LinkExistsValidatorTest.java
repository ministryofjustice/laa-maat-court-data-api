package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LinkExistsValidatorTest {


    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @InjectMocks
    private LinkExistsValidator linkExistsValidator;



    @Test
    public void testWhenLinkNotExists_validationPasses() {

        Integer testId = 1000;
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(testId)).thenReturn(BigDecimal.ZERO.intValue());
        Optional result = linkExistsValidator.validate(testId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    public void testWhenLinkAlreadyExists_throwsException() {
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt()))
                .thenReturn(BigDecimal.ONE.intValue());

        Assertions.assertThrows(ValidationException.class, ()->
                linkExistsValidator.validate(Mockito.anyInt()),"0 is already linked to a case.");
    }

}
