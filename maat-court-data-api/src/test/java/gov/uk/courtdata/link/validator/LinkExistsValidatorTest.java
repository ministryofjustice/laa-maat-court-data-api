package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class LinkExistsValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @InjectMocks
    private LinkExistsValidator linkExistsValidator;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenLinkNotExists_validationPasses() {

        Integer testId = 1000;
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(testId)).thenReturn(BigDecimal.ZERO.intValue());
        Optional result = linkExistsValidator.validate(testId);
        Assert.assertFalse(result.isPresent());

    }

    @Test
    public void testWhenLinkAlreadyExists_throwsException() {
        Mockito.when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt()))
                .thenReturn(BigDecimal.ONE.intValue());

        thrown.expect(ValidationException.class);
        thrown.expectMessage("MaatId already linked to the application.");
        linkExistsValidator.validate(Mockito.anyInt());
    }

}
