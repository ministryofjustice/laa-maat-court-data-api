package gov.uk.courtdata.validator;


import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.validator.LinkRegisterValidator;
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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinkRegisterValidatorTest {

    @InjectMocks
    private LinkRegisterValidator linkRegisterValidator;
    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenZeroLinks_whenValidatorsIsInvoked_thenValidationIsFailed() {


        when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt()))
                .thenReturn(0);
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There is No Link Available for  MAAT ID : " + 10);
        linkRegisterValidator.validate(10);
    }

    @Test
    public void givenMoreThanOneLinksAvailable_whenValidatorsIsInvoked_thenValidationIsFailed() {


        when(wqLinkRegisterRepository.getCountByMaatId(Mockito.anyInt()))
                .thenReturn(2);
        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("There are multiple Links found for  MAAT ID : " + 10);
        linkRegisterValidator.validate(10);
    }
}
