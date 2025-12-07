package gov.uk.courtdata.iojappeal.validator;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IOJAppealValidationProcessorTest {

    @InjectMocks
    private IOJAppealValidationProcessor iojAppealValidationProcessor;

    @Mock
    private IOJAppealRepository iojAppealRepository;

    @Test
    void givenAnIOJUpdateWithExistingEntityInDBAndNullModifiedDate_ThenDoNothing() {
        //given
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject();
        var dbEntity = TestEntityDataBuilder.getIOJAppealEntity("IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test
    void givenAnIOJUpdateWithExistingEntityInDBAndHasMatchingModifiedDate_ThenDoNothing() {
        //given
        var matchingDateModified = LocalDateTime.of(2023, 1, 1, 10, 0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(matchingDateModified);
        var dbEntity = TestEntityDataBuilder.getIoJAppealEntity(matchingDateModified, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));

        iojAppealValidationProcessor.validate(updateIOJAppealObject);
    }

    @Test
    void givenAnIOJUpdateWithNonExistingRecordInDB_ThenThrowValidationException() {
        when(iojAppealRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(ValidationException.class, () -> {
            iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
        });
    }

    @Test
    void givenAnIOJUpdateWithDifferentModifiedDateAsOnDB_ThenThrowValidationException() {
        var mismatchedDateModified = LocalDateTime.of(2023, 1, 1, 10, 0);
        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(TestEntityDataBuilder.getIoJAppealEntity(mismatchedDateModified, "IN PROGRESS")));
        Assertions.assertThrows(ValidationException.class, () -> {
            iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
        });
    }

    @Test
    void givenAnIOJUpdateWithNullModifiedDate_andDBEntityWithModifiedDate_ThenThrowValidationException() {
        //given
        var dbEntityDateModified = LocalDateTime.of(2023, 1, 1, 10, 0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(null);
        var dbEntity = TestEntityDataBuilder.getIoJAppealEntity(dbEntityDateModified, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            iojAppealValidationProcessor.validate(updateIOJAppealObject);
        });
    }

    @Test
    void givenAnIOJUpdateWithModifiedDate_andDBEntityWithNullModifiedDate_ThenThrowValidationException() {
        //given
        var updateEntityDateModified = LocalDateTime.of(2023, 1, 1, 10, 0);
        var updateIOJAppealObject = TestModelDataBuilder.getUpdateIOJAppealObject(updateEntityDateModified);
        var dbEntity = TestEntityDataBuilder.getIoJAppealEntity(null, "IN PROGRESS");

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(dbEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            iojAppealValidationProcessor.validate(updateIOJAppealObject);
        });
    }

    @Test
    void givenAnIOJUpdateWithStatusCompleteInDB_ThenThrowValidationException() {

        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(TestEntityDataBuilder.getIOJAppealEntity("COMPLETE")));
        Assertions.assertThrows(ValidationException.class, () -> {
            iojAppealValidationProcessor.validate(TestModelDataBuilder.getUpdateIOJAppealObject());
        });
    }
}