package gov.uk.courtdata.iojAppeal.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.IOJAppealIdValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealValidationProcessorTest {

    @InjectMocks
    private IOJAppealValidationProcessor iojAppealValidationProcessor;

    @Mock
    private IOJAppealIdValidator iojAppealIdValidator;

    @Test
    public void givenValidIOJAppealID_ThenIOJAppealValidationProcessorReturnsEmptyOptional(){
        when(iojAppealIdValidator.validate(any())).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), iojAppealValidationProcessor.validate(IOJ_APPEAL_ID));
    }

    @Test(expected= ValidationException.class)
    public void givenNonExistentIOJAppealID_ThenIOJAppealValidationProcessorThrowsException(){
        when(iojAppealIdValidator.validate(any())).thenThrow(new ValidationException());
        iojAppealValidationProcessor.validate(IOJ_APPEAL_ID+1);
    }

    @Test(expected= ValidationException.class)
    public void givenNullIOJAppealID_thenIOJAppealValidationProcessorThrowsException(){
        when(iojAppealIdValidator.validate(null)).thenThrow(new ValidationException());
        iojAppealValidationProcessor.validate(null);
    }
    @Test(expected= ValidationException.class)
    public void givenInvalidIOJAppealID_thenIOJAppealValidationProcessorThrowsException(){
        when(iojAppealIdValidator.validate(any())).thenThrow(new ValidationException());
        iojAppealValidationProcessor.validate(-1);
    }

}