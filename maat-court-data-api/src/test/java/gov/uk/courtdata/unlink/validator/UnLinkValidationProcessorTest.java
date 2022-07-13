package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnLinkValidationProcessorTest {

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkRegisterValidator linkRegisterValidator;
    @Mock
    private ReasonValidator reasonValidator;
    @Mock
    private UserIdValidator userIdValidator;

    @InjectMocks
    private UnLinkValidationProcessor unLinkValidationProcessor;


    @Test
    public void testWhenMaatIdValidatorFails_throwsValidationException() {
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(999888)
                .build();

        when(maatIdValidator.validate(123456))
                .thenThrow(new ValidationException("MAAT id is missing."));

        Assertions.assertThrows(ValidationException.class,()->
                unLinkValidationProcessor.validate(unlink),
                "MAAT id is missing.");



    }

    @Test
    public void testWhenLinkRegisterValidatorFails_throwsValidationException() {
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(999888)
                .build();

        when(linkRegisterValidator.validate(unlink.getMaatId()))
                .thenThrow(new MAATCourtDataException("MAAT id is missing."));

        Assertions.assertThrows(MAATCourtDataException.class,()->
                unLinkValidationProcessor.validate(unlink),"MAAT id is missing.");
    }

    @Test
    public void testWhenReasonIdValidatorFails_throwsValidationException() {
        Unlink unlink = Unlink.builder()
                .reasonId(999888)
                .build();

        when(reasonValidator.validate(999888))
                .thenThrow(new ValidationException("Reasons id is missing."));
        Assertions.assertThrows(ValidationException.class,()->
                unLinkValidationProcessor.validate(unlink),"Reasons id is missing.");

    }

    @Test
    public void testWhenUserIdValidatorFails_throwsValidationException() {
        Unlink unlink = Unlink.builder()
                .userId("laa-moj")
                .reasonId(12121)
                .maatId(4444)
                .build();

        when(userIdValidator.validate("laa-moj"))
                .thenThrow(new ValidationException("User id is missing."));

        Assertions.assertThrows(ValidationException.class,()->
        unLinkValidationProcessor.validate(unlink),"User id is missing.");

    }

    @Test
    public void testWhenMaatIdValidatorFails_valdationPasses() {
        final int maatId = 123456;
        final int reasonId = 88999;
        final String userId = "moj";
        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(88999)
                .userId("moj")
                .build();

        when(maatIdValidator.validate(maatId)).thenReturn(Optional.empty());
        when(reasonValidator.validate(reasonId)).thenReturn(Optional.empty());
        when(userIdValidator.validate(userId)).thenReturn(Optional.empty());
        when(linkRegisterValidator.validate(maatId)).thenReturn(Optional.empty());
        unLinkValidationProcessor.validate(unlink);
        //then
        verify(maatIdValidator, times(1)).validate(maatId);
        verify(reasonValidator, times(1)).validate(reasonId);
        verify(userIdValidator, times(1)).validate(userId);
        verify(linkRegisterValidator, times(1)).validate(maatId);

    }

    @Test
    public void givenWQLinkRegisterHasMoreThanOneEntry_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        WqLinkRegisterEntity wqLinkRegisterEntity1 = WqLinkRegisterEntity.builder().build();
        WqLinkRegisterEntity wqLinkRegisterEntity2 = WqLinkRegisterEntity.builder().build();
        List<WqLinkRegisterEntity> linkRegisterEntities = new ArrayList<>();
        linkRegisterEntities.add(wqLinkRegisterEntity1);
        linkRegisterEntities.add(wqLinkRegisterEntity2);
        Assertions.assertThrows(MAATCourtDataException.class,()->
        unLinkValidationProcessor.validateWQLinkRegister(linkRegisterEntities, 123),
                "There are multiple links found for  MAAT ID : 123");
    }

    @Test
    public void givenWQLinkRegisterIsNull_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {

        Assertions.assertThrows(MAATCourtDataException.class,()->
                unLinkValidationProcessor.validateWQLinkRegister(null, 123),
                "There is No link established for MAAT ID : 123");

    }

    @Test
    public void givenWQLinkRegisterIsEmpty_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {
        List<WqLinkRegisterEntity> linkRegisterEntities = new ArrayList<>();
        Assertions.assertThrows(MAATCourtDataException.class,()->{
            unLinkValidationProcessor.validateWQLinkRegister(linkRegisterEntities, 123);
        },"There is No link established for MAAT ID : 123");
    }

    @Test
    public void givenUnLinkRequestIsIsNull_whenValidateWQLinkRegisterIsInvoked_thenValidationFailed() {

        Assertions.assertThrows(MAATCourtDataException.class,()->{
            unLinkValidationProcessor.validate(null);
        },"Unlink Request is empty");
    }


}
