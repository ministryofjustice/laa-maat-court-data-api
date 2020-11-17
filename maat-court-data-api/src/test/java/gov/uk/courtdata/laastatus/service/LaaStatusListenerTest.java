package gov.uk.courtdata.laastatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.laastatus.builder.CourtDataDTOBuilder;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations="classpath:application.yaml")
public class LaaStatusListenerTest {

    @InjectMocks
    private LaaStatusListener laaStatusListener;
    @Mock
    private Gson gson;
    @Mock
    private LaaStatusService laaStatusService;

    @Mock
    private CourtDataDTOBuilder courtDataDTOBuilder;

    @Mock
    LaaStatusPostCDAService laaStatusPostCDAService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenSQSIsReceived_whenLaaStatusListenerIsInvoked_thenPostMessageToCDA() {

        String sqsMessage = "some sqs message";

        CaseDetails caseDetails = getCaseDetails(true);

        CourtDataDTO courtDataDTO = getCourtDataDTO(caseDetails);
        when(gson.fromJson(sqsMessage, CaseDetails.class)).thenReturn(caseDetails);

        when(courtDataDTOBuilder.build(caseDetails)).thenReturn(courtDataDTO);
        laaStatusListener.receive(sqsMessage);

        verify(laaStatusPostCDAService).process(courtDataDTO);
        verify(queueMessageLogService).createLog(MessageType.LAA_STATUS, sqsMessage);
    }


    @Test
    public void givenSQSIsReceived_whenLaaStatusListenerIsInvoked_thenPostMessageToCDAAndCrimeApps() {

        String sqsMessage = "some sqs message";

        CaseDetails caseDetails = getCaseDetails(false);

        CourtDataDTO courtDataDTO = getCourtDataDTO(caseDetails);
        when(gson.fromJson(sqsMessage, CaseDetails.class)).thenReturn(caseDetails);

        when(courtDataDTOBuilder.build(caseDetails)).thenReturn(courtDataDTO);
        laaStatusListener.receive(sqsMessage);

        verify(laaStatusPostCDAService).process(courtDataDTO);
        verify(laaStatusService).execute(courtDataDTO);
        verify(courtDataDTOBuilder).build(any());
        verify(gson).fromJson(sqsMessage,CaseDetails.class);

        verify(queueMessageLogService).createLog(MessageType.LAA_STATUS, sqsMessage);

    }

    private CourtDataDTO getCourtDataDTO(CaseDetails caseDetails) {

        return CourtDataDTO.builder()
                .caseDetails(caseDetails)
                .caseId(121211)
                .libraId("2323223")
                .build();
    }

    private CaseDetails getCaseDetails(boolean action) {

        return CaseDetails.builder()
                .caseUrn("343")
                .defendant(Defendant.builder().build())
                .onlyForCDAService(action)
                .build();
    }
}