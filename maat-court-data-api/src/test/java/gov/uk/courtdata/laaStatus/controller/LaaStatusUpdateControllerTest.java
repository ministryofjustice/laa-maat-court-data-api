package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.laaStatus.service.LaaStatusPublisher;
import gov.uk.courtdata.laaStatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LaaStatusUpdateControllerTest {

    @InjectMocks
    private LaaStatusUpdateController laaStatusUpdateController;

    @Mock
    private LaaStatusValidationProcessor laaStatusValidationProcessor;
    @Mock
    private Gson gson;
    @Mock
    private LaaStatusPublisher laaStatusPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        final MessageCollection messageCollection = MessageCollection.builder().messages(new ArrayList<>()).build();
        String myString = "Test JSON";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus(myString);

        //then
        verify(laaStatusPublisher, times(1)).publish(caseDetails);

    }

    @Test
    public void givenValidationIsFailed_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        String message = "Test Validation Message";
        List<String> validationMessages = new ArrayList<>();
        validationMessages.add(message);
        final MessageCollection messageCollection = MessageCollection.builder().messages(validationMessages).build();
        String myString = "Test JSON";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus(myString);

        //then
        verify(laaStatusPublisher, times(0)).publish(caseDetails);

    }
}
