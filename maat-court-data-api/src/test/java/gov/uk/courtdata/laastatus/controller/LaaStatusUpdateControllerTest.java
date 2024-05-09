package gov.uk.courtdata.laastatus.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusServiceUpdate;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.service.QueueMessageLogService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LaaStatusUpdateControllerTest {

    @Mock
    private LaaStatusValidationProcessor laaStatusValidationProcessor;

    @Mock
    private LaaStatusServiceUpdate laaStatusServiceUpdate;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    private LaaStatusUpdateController laaStatusUpdateController;

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        laaStatusUpdateController = new LaaStatusUpdateController(laaStatusValidationProcessor,
            gson,
            queueMessageLogService,
            laaStatusServiceUpdate);
    }

    @Test
    public void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled() {

        //given
        final MessageCollection messageCollection = MessageCollection.builder().messages(new ArrayList<>()).build();
        String caseDetailsJson = """
            {
              "maatId": 5635539,
              "caseUrn": "EITHERWAY",
              "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
            }
                                      """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        //when
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        MessageCollection messageResponse =
            laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c",
                caseDetailsJson);

        //then
        assertAll(
            () -> verify(laaStatusValidationProcessor).validate(any()),
            () -> verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(caseDetails),
            () -> assertThat(messageResponse.getMessages().size()).isZero()
        );
    }

    @Test
    public void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled1() {

        //given
        final MessageCollection messageCollection = MessageCollection.builder().messages(new ArrayList<>()).build();
        String caseDetailsJson = """
            {
              "maatId": 5635539,
              "caseUrn": "EITHERWAY"
            }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        //when
        when(laaStatusValidationProcessor.validate(
            any(CaseDetails.class))) // need to use any as the CaseDetails are updated with a UUID
            .thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus(null, caseDetailsJson);

        //then
        assertAll(
            () -> verify(laaStatusValidationProcessor).validate(any()),
            () -> verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(any(CaseDetails.class))
        );
    }

    @Test
    public void givenValidationIsFailed_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        //given
        List<String> validationMessages = List.of("Test Validation Message");
        final MessageCollection messageCollection = MessageCollection.builder().messages(validationMessages).build();
        String caseDetailsJson = """
            {
              "maatId": 5635539,
              "caseUrn": "EITHERWAY",
              "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
            }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        //when
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c",
            caseDetailsJson);

        //then
        assertAll(
            () -> verify(laaStatusValidationProcessor).validate(any()),
            () -> assertThat(messageCollection.getMessages().get(0)).isEqualTo(
                "Test Validation Message"),
            () -> verify(laaStatusServiceUpdate, times(0)).updateMlaAndCDA(caseDetails)
        );
    }

    @Test
    public void givenExceptionIsRaised_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        //given
        String caseDetailsJson = """
            {
              "maatId": 5635539,
              "caseUrn": "EITHERWAY",
              "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
            }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        //when
        when(laaStatusValidationProcessor.validate(caseDetails))
            .thenThrow(new MAATCourtDataException("Validation Failed"));

        MAATCourtDataException maatCourtDataException = Assertions.assertThrows(
            MAATCourtDataException.class, () ->
                laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c",
                    caseDetailsJson));

        //then
        assertEquals("MAAT API Call failed - Validation Failed",
            maatCourtDataException.getMessage());
    }
}

