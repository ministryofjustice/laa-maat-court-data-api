package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.IOJAppealRepository;
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
public class IOJAppealIdValidatorTest {

    @InjectMocks
    private IOJAppealIdValidator iojAppealIdValidator;

    @Mock
    private IOJAppealRepository iojAppealRepository;

    @Test
    public void givenValidIOJAppealID_thenIOJAppealIdValidatorReturnsEmptyOptional(){
        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(new IOJAppealEntity()));
        assertEquals(Optional.empty(), iojAppealIdValidator.validate(IOJ_APPEAL_ID));
    }

    @Test(expected= ValidationException.class)
    public void givenNonExistentIOJAppealID_thenIOJAppealIdValidatorThrowsException(){
        when(iojAppealRepository.findById(any())).thenReturn(Optional.empty());
        iojAppealIdValidator.validate(IOJ_APPEAL_ID+1);
    }

    @Test(expected= ValidationException.class)
    public void givenNullIOJAppealID_thenIOJAppealIdValidatorThrowsException(){
        iojAppealIdValidator.validate(null);
    }

    @Test(expected= ValidationException.class)
    public void givenInvalidIOJAppealID_thenIOJAppealIdValidatorThrowsException(){
        iojAppealIdValidator.validate(-1);
    }
}