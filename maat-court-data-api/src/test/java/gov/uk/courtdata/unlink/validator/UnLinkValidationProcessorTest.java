package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.link.validator.LinkExistsValidator;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnLinkValidationProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkExistsValidator linkExistsValidator;
    @Mock
    private ReasonValidator reasonValidator;

    @InjectMocks
    private UnLinkValidationProcessor unLinkValidationProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenMaatIdValidatorFails_throwsValdationException() {
        final int maatId = 123456;
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(999888)
                .build();

        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");

        when(maatIdValidator.validate(123456))
                .thenThrow(new ValidationException("MAAT id is missing."));

        unLinkValidationProcessor.validate(unlink);

    }

    @Test
    public void testWhenReasonIdValidatorFails_throwsValdationException() {
        final int reasonId = 999888;
        Unlink unlink = Unlink.builder()
                .reasonId(999888)
                .build();

        thrown.expect(ValidationException.class);
        thrown.expectMessage("Reasons id is missing.");

        when(reasonValidator.validate(999888))
                .thenThrow(new ValidationException("Reasons id is missing."));

        unLinkValidationProcessor.validate(unlink);

    }

    @Test
    public void testWhenMaatIdValidatorFails_valdationPasses() {
        final int maatId = 123456;
        final int reasonId=88999;
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(88999)
                .build();

        when(maatIdValidator.validate(maatId)).thenReturn(Optional.empty());
        when(linkExistsValidator.validate(maatId)).thenReturn(Optional.empty());
        when(reasonValidator.validate(reasonId)).thenReturn(Optional.empty());
        unLinkValidationProcessor.validate(unlink);
        //then
        verify(maatIdValidator, times(1)).validate(maatId);
        verify(linkExistsValidator, times(1)).validate(maatId);
        verify(reasonValidator, times(1)).validate(reasonId);

    }

    @After
    public void tearDown() throws Exception {

    }


}
