package gov.uk.courtdata.laastatus.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusServiceUpdate;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.service.QueueMessageLogService;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
class LaaStatusUpdateControllerTest {

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
        laaStatusUpdateController = new LaaStatusUpdateController(
                laaStatusValidationProcessor, gson, queueMessageLogService, laaStatusServiceUpdate);
    }

    @Test
    void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled() {

        // given
        final MessageCollection messageCollection =
                MessageCollection.builder().messages(new ArrayList<>()).build();
        String caseDetailsJson =
                """
                        {
                          "maatId": 5635539,
                          "caseUrn": "EITHERWAY",
                          "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
                        }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        // when
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        MessageCollection messageResponse =
                laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c", caseDetailsJson);

        // then
        SoftAssertions.assertSoftly(s -> {
            verify(laaStatusValidationProcessor).validate(any());
            verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(caseDetails);
            assertThat(messageResponse.getMessages()).isEmpty();
        });
    }

    @Test
    void givenValidationIsPassed_whenControllerIsInvoked_thenPublisherIsCalled1() {

        // given
        final MessageCollection messageCollection =
                MessageCollection.builder().messages(new ArrayList<>()).build();
        String caseDetailsJson =
                """
                        {
                          "maatId": 5635539,
                          "caseUrn": "EITHERWAY"
                        }
                        """;

        // when
        when(laaStatusValidationProcessor.validate(
                        any(CaseDetails.class))) // need to use any as the CaseDetails are updated with a UUID
                .thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus(null, caseDetailsJson);

        // then
        SoftAssertions.assertSoftly(s -> {
            verify(laaStatusValidationProcessor).validate(any());
            verify(laaStatusServiceUpdate, times(1)).updateMlaAndCDA(any(CaseDetails.class));
        });
    }

    @Test
    void givenValidationIsFailed_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        // given
        List<String> validationMessages = List.of("Test Validation Message");
        final MessageCollection messageCollection =
                MessageCollection.builder().messages(validationMessages).build();
        String caseDetailsJson =
                """
                        {
                          "maatId": 5635539,
                          "caseUrn": "EITHERWAY",
                          "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
                        }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        // when
        when(laaStatusValidationProcessor.validate(caseDetails)).thenReturn(messageCollection);

        laaStatusUpdateController.updateLAAStatus("48e60e52-70f9-415d-8c57-c25a16419a7c", caseDetailsJson);

        // then
        SoftAssertions.assertSoftly(s -> {
            verify(laaStatusValidationProcessor).validate(any());
            assertThat(messageCollection.getMessages().getFirst()).isEqualTo("Test Validation Message");
            verify(laaStatusServiceUpdate, never()).updateMlaAndCDA(caseDetails);
        });
    }

    @Test
    void givenExceptionIsRaised_whenControllerIsInvoked_thenPublisherIsNOTCalled() {

        // given
        String caseDetailsJson =
                """
                        {
                          "maatId": 5635539,
                          "caseUrn": "EITHERWAY",
                          "laaTransactionId": "48e60e52-70f9-415d-8c57-c25a16419a7c"
                        }
                        """;
        final CaseDetails caseDetails = gson.fromJson(caseDetailsJson, CaseDetails.class);

        // when
        when(laaStatusValidationProcessor.validate(caseDetails))
                .thenThrow(new MAATCourtDataException("Validation Failed"));

        MAATCourtDataException maatCourtDataException = Assertions.assertThrows(
                MAATCourtDataException.class,
                () -> laaStatusUpdateController.updateLAAStatus(
                        "48e60e52-70f9-415d-8c57-c25a16419a7c", caseDetailsJson));

        // then
        assertThat(maatCourtDataException.getMessage()).isEqualTo("MAAT API Call failed - Validation Failed");
    }
}
