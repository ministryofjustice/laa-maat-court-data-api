package gov.uk.courtdata.laaStatus.validator;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LaaStatusValidationProcessorTest {


    @InjectMocks
    private LaaStatusValidationProcessor laaStatusValidationProcessor;

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkRegisterValidator linkRegisterValidator;
    @Mock
    private SolicitorValidator solicitorValidator;
    @Mock
    private LaaStatusValidator laaStatusValidator;
    @Mock
    private DefendantValidator defendantValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenLAAStatusValidators_whenProcessorIsInvoked_thenRequiredValidatorsAreInvoked() {

        //given
        final int testMaatId = 1000;
        final CaseDetails caseDetails = CaseDetails.builder().maatId(testMaatId).build();

        // when
        laaStatusValidationProcessor.validate(caseDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkRegisterValidator, times(1)).validate(testMaatId);
        verify(defendantValidator, times(1)).validate(testMaatId);
        verify(solicitorValidator, times(1)).validate(caseDetails);
        verify(laaStatusValidator, times(1)).validate(caseDetails);

    }
}
