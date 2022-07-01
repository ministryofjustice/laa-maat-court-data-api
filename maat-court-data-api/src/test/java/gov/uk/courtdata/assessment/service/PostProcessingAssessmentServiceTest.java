package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.model.assessment.PostProcessing;
import gov.uk.courtdata.repository.PostProcessingStoredProcedureRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PostProcessingAssessmentServiceTest {

    @InjectMocks
    private PostProcessingService postProcessingService;
    @Mock
    private PostProcessingStoredProcedureRepository postProcessingStoredProcedureRepository;

    @Test
    public void whenExecuteIsInvoked_thenPostProcessingProcedureIsCalled() {
        PostProcessing postProcessing = TestModelDataBuilder.getPostProcessing();
        postProcessingService.execute(postProcessing);
        verify(postProcessingStoredProcedureRepository).invokePostAssessmentProcessingCma(postProcessing);
    }

    @Test
    public void givenIssuesWithStoredProcedure_whenExecuteIsInvoked_thenExceptionIsLoggedAndThrown() {
        PostProcessing postProcessing = TestModelDataBuilder.getPostProcessing();
        doThrow(new JpaSystemException(new RuntimeException("Problem Calling Stored Procedure")))
                .when(postProcessingStoredProcedureRepository).invokePostAssessmentProcessingCma(postProcessing);
        Assert.assertThrows(DataAccessException.class,
                () -> postProcessingStoredProcedureRepository.invokePostAssessmentProcessingCma(postProcessing));
    }
}
