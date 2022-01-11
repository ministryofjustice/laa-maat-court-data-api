package gov.uk.courtdata.iojAppeal.validator;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealValidationProcessorTest {

    @InjectMocks
    private IOJAppealValidationProcessor iojAppealValidationProcessor;

    @Mock
    private IOJAppealRepository iojAppealRepository;

    @Test
    public void givenAnIOJUpdateWithExistingEntityInDBAndNullModifiedDate_ThenDoNothing(){
        //given
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject();
        var dbEntity = TestEntityDataBuilder.getIOJAppealEntity(  "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test
    public void givenAnIOJUpdateWithExistingEntityInDBAndHasMatchingModifiedDate_ThenDoNothing(){
        //given
        var matchingDateModified = LocalDateTime.of(2023,1,1,10,0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(matchingDateModified);
        var dbEntity = TestEntityDataBuilder.getIOJAppealEntity( matchingDateModified, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test(expected = ValidationException.class)
    public void givenAnIOJUpdateWithNonExistingRecordInDB_ThenThrowValidationException(){
        when(iojAppealRepository.findById(any())).thenReturn(Optional.empty());

        iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
    }

    @Test(expected = ValidationException.class)
    public void givenAnIOJUpdateWithDifferentModifiedDateAsOnDB_ThenThrowValidationException(){
        var mismatchedDateModified = LocalDateTime.of(2023,1,1,10,0);
        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(TestEntityDataBuilder.getIOJAppealEntity(mismatchedDateModified,"IN PROGRESS")));

        iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
    }

    @Test(expected = ValidationException.class)
    public void givenAnIOJUpdateWithNullModifiedDate_andDBEntityWithModifiedDate_ThenThrowValidationException(){
        //given
        var dbEntityDateModified = LocalDateTime.of(2023,1,1,10,0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(null);
        var dbEntity = TestEntityDataBuilder.getIOJAppealEntity( dbEntityDateModified, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test(expected = ValidationException.class)
    public void givenAnIOJUpdateWithModifiedDate_andDBEntityWithNullModifiedDate_ThenThrowValidationException(){
        //given
        var updateEntityDateModified = LocalDateTime.of(2023,1,1,10,0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(updateEntityDateModified);
        var dbEntity = TestEntityDataBuilder.getIOJAppealEntity( null, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test(expected = ValidationException.class)
    public void givenAnIOJUpdateWithStatusCompleteInDB_ThenThrowValidationException(){

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(TestEntityDataBuilder.getIOJAppealEntity("COMPLETE")));

        iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
    }
}