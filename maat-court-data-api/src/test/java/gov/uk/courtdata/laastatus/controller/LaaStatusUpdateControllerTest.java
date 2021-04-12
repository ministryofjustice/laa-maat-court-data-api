
package gov.uk.courtdata.laastatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusServiceUpdate;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private LaaStatusServiceUpdate laaStatusServiceUpdate;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        final MessageCollection messageCollection = MessageCollection.builder().messages(new ArrayList<>()).build();
        String myString = "{\"maatId\": 5635539,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  \"laaTransactionId\": \"48e60e52-70f9-415d-8c57-c25a16419a7c\"}";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);


        MessageCollection messageResponse =
                laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c", myString);

        //then
        verify(laaStatusValidationProcessor).validate(any());
        verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(caseDetails);
        assertThat(messageResponse.getMessages().size()).isZero();
    }

    @Test
    public void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled1() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        final MessageCollection messageCollection = MessageCollection.builder().messages(new ArrayList<>()).build();
        String myString = "{\"maatId\": 5635539,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  }";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus(null, myString);

        //then
        verify(laaStatusValidationProcessor).validate(any());
        verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(caseDetails);
    }

    @Test
    public void givenValidationIsFailed_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        String message = "Test Validation Message";
        List<String> validationMessages = new ArrayList<>();
        validationMessages.add(message);
        final MessageCollection messageCollection = MessageCollection.builder().messages(validationMessages).build();
        String myString = "{\"maatId\": 5635539,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  \"laaTransactionId\": \"48e60e52-70f9-415d-8c57-c25a16419a7c\"}";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c", myString);

        //then
        verify(laaStatusValidationProcessor).validate(any());
        assertThat(messageCollection.getMessages().get(0)).isEqualTo("Test Validation Message");
        verify(laaStatusServiceUpdate, times(0)).updateMlaAndCDA(caseDetails);
    }

    @Test
    public void givenExceptionIsRaised_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        //given
        final CaseDetails caseDetails = CaseDetails.builder().build();
        String myString = "{\"maatId\": 5635539,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  \"laaTransactionId\": \"48e60e52-70f9-415d-8c57-c25a16419a7c\"}";

        //when
        when(gson.fromJson(myString, CaseDetails.class)).thenReturn(caseDetails);
        when(laaStatusValidationProcessor.validate(caseDetails)).thenThrow(new MAATCourtDataException("Validation Failed"));

        exception.expect(MAATCourtDataException.class);
        laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c", myString);
    }
}

