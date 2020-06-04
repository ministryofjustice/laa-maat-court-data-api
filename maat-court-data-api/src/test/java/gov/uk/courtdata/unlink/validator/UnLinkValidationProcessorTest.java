package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
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

import java.util.ArrayList;
import java.util.List;
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
    @Mock
    private UserIdValidator userIdValidator;

    @InjectMocks
    private UnLinkValidationProcessor unLinkValidationProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenMaatIdValidatorFails_throwsValdationException() {
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
    public void testWhenReasonIdValidatorFails_throwsValidationException() {
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
    public void testWhenUserIdValidatorFails_throwsValidationException() {
        Unlink unlink = Unlink.builder()
                .userId("laa-moj")
                .reasonId(12121)
                .maatId(4444)
                .build();

        thrown.expect(ValidationException.class);
        thrown.expectMessage("User id is missing.");

        when(userIdValidator.validate("laa-moj"))
                .thenThrow(new ValidationException("User id is missing."));

        unLinkValidationProcessor.validate(unlink);

    }

    @Test
    public void testWhenMaatIdValidatorFails_valdationPasses() {
        final int maatId = 123456;
        final int reasonId=88999;
        final String userId="moj";
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(88999)
                .userId("moj")
                .build();

        when(maatIdValidator.validate(maatId)).thenReturn(Optional.empty());
        when(linkExistsValidator.validate(maatId)).thenReturn(Optional.empty());
        when(reasonValidator.validate(reasonId)).thenReturn(Optional.empty());
        when(userIdValidator.validate(userId)).thenReturn(Optional.empty());
        unLinkValidationProcessor.validate(unlink);
        //then
        verify(maatIdValidator, times(1)).validate(maatId);
        verify(linkExistsValidator, times(1)).validate(maatId);
        verify(reasonValidator, times(1)).validate(reasonId);
        verify(userIdValidator, times(1)).validate(userId);

    }

    @Test
    public void givenWQLinkRegisterHasMoreThanOneEntry_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        thrown.expect(MAATCourtDataException.class);
        thrown.expectMessage("There are multiple links found for  MAAT ID : 123");
        WqLinkRegisterEntity wqLinkRegisterEntity1 = WqLinkRegisterEntity.builder().build();
        WqLinkRegisterEntity wqLinkRegisterEntity2 = WqLinkRegisterEntity.builder().build();
        List<WqLinkRegisterEntity> linkRegisterEntities = new ArrayList<>();
        linkRegisterEntities.add(wqLinkRegisterEntity1);
        linkRegisterEntities.add(wqLinkRegisterEntity2);
        unLinkValidationProcessor.validateWQLinkRegister(linkRegisterEntities,123);
    }

    @Test
    public void givenWQLinkRegisterIsNull_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        thrown.expect(MAATCourtDataException.class);
        thrown.expectMessage("There is No link established for MAAT ID : 123");
        unLinkValidationProcessor.validateWQLinkRegister(null,123);
    }

    @After
    public void tearDown() throws Exception {

    }


}
