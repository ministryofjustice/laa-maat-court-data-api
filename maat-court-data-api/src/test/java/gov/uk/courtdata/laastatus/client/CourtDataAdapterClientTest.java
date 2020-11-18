package gov.uk.courtdata.laastatus.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourtDataAdapterClientTest {


    @InjectMocks
    private CourtDataAdapterClient courtDataAdapterClient;

    @Mock
    private GsonBuilder gsonBuilder;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process() {

        String messageStr = "";
        when(gsonBuilder.create().toJson(newQueueMessage(23232))).thenReturn(messageStr);
        queueMessageLogService.createLog(MessageType.LAA_STATUS_UPDATE, newQueueMessage(2323));

    }

    private String newQueueMessage(Integer maatId) {

        return new StringBuilder()
                .append("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":")
                .append(maatId)
                .append("}")
                .toString();
    }


}