package gov.uk.courtdata.laastatus.validator;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
        verify(solicitorValidator, times(1)).validate(testMaatId);
        verify(laaStatusValidator, times(1)).validate(caseDetails);

    }
}
