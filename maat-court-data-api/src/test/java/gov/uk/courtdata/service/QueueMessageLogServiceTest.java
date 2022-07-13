package gov.uk.courtdata.service;


 import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class QueueMessageLogServiceTest {

    @InjectMocks
    public QueueMessageLogService queueMessageLogService;
    @Spy
    public QueueMessageLogRepository queueMessageLogRepository;
    @Captor
    private ArgumentCaptor<QueueMessageLogEntity> queueMessageCaptor;


    @Test
    public void whenLinkMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.LINK, newQueueMessage(maatId));

        verify(queueMessageLogRepository, times(1)).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        assertAll("linkMessage",
                () -> assertNotNull(savedQueueMsg),
                () -> assertNotNull(savedQueueMsg.getMaatId()),
                () -> assertNotNull(savedQueueMsg.getTransactionUUID()),
                () -> assertNotNull(savedQueueMsg.getType()),
                () -> assertNotNull(savedQueueMsg.getMessage()),
                () -> assertNotNull(savedQueueMsg.getCreatedTime()),
                () -> assertEquals(savedQueueMsg.getMaatId(), maatId),
                () -> assertEquals(savedQueueMsg.getType(), MessageType.LINK.name())

        );
    }

    @Test
    public void whenUnLinkMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.UNLINK, newQueueMessage(maatId));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        assertAll("unLinkMessage",
                () -> assertNotNull(savedQueueMsg),
                () -> assertNotNull(savedQueueMsg.getMaatId()),
                () -> assertNotNull(savedQueueMsg.getTransactionUUID()),
                () -> assertNotNull(savedQueueMsg.getType()),
                () -> assertNotNull(savedQueueMsg.getMessage()),
                () -> assertNotNull(savedQueueMsg.getCreatedTime()),
                () -> assertEquals(savedQueueMsg.getMaatId(), maatId),
                () -> assertEquals(savedQueueMsg.getType(), MessageType.UNLINK.name())

        );
    }

    @Test
    public void whenHearingMessage_CheckLogEntryCreatedForMagsCourt() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.HEARING,
                newHearingQueueMessage(maatId, JurisdictionType.MAGISTRATES));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        assertAll("hearingMagsCourtMessage",
                () -> assertNotNull(savedQueueMsg),
                () -> assertNotNull(savedQueueMsg.getMaatId()),
                () -> assertNotNull(savedQueueMsg.getTransactionUUID()),
                () -> assertNotNull(savedQueueMsg.getType()),
                () -> assertNotNull(savedQueueMsg.getMessage()),
                () -> assertNotNull(savedQueueMsg.getCreatedTime()),
                () -> assertEquals(savedQueueMsg.getMaatId(), maatId),
                () -> assertEquals(savedQueueMsg.getType(), expectedType(JurisdictionType.MAGISTRATES)
                )

        );
    }

    @Test
    public void whenHearingMessage_CheckLogEntryCreatedCrownCourt() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.HEARING,
                newHearingQueueMessage(maatId, JurisdictionType.CROWN));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        assertAll("hearingCrownCourtMessage",
                () -> assertNotNull(savedQueueMsg),
                () -> assertNotNull(savedQueueMsg.getMaatId()),
                () -> assertNotNull(savedQueueMsg.getTransactionUUID()),
                () -> assertNotNull(savedQueueMsg.getType()),
                () -> assertNotNull(savedQueueMsg.getMessage()),
                () -> assertNotNull(savedQueueMsg.getCreatedTime()),
                () -> assertEquals(savedQueueMsg.getMaatId(), maatId),
                () -> assertEquals(savedQueueMsg.getType(), expectedType(JurisdictionType.CROWN))

        );
    }


    @Test
    public void whenLaaStatusMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.LAA_STATUS, newQueueMessage(maatId));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        assertAll("linkMessage",
                () -> assertNotNull(savedQueueMsg),
                () -> assertNotNull(savedQueueMsg.getMaatId()),
                () -> assertNotNull(savedQueueMsg.getTransactionUUID()),
                () -> assertNotNull(savedQueueMsg.getType()),
                () -> assertNotNull(savedQueueMsg.getMessage()),
                () -> assertNotNull(savedQueueMsg.getCreatedTime()),
                () -> assertEquals(savedQueueMsg.getMaatId(), maatId),
                () -> assertEquals(savedQueueMsg.getType(), MessageType.LAA_STATUS.name())

        );
    }

    private String newQueueMessage(Integer maatId) {

        return new StringBuilder()
                .append("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":")
                .append(maatId)
                .append("}")
                .toString();
    }

    private String newHearingQueueMessage(Integer maatId, JurisdictionType jurisdictionType) {

        return new StringBuilder()
                .append("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":")
                .append(maatId)
                .append(",\"jurisdictionType\":")
                .append("\"")
                .append(jurisdictionType.name())
                .append("\"")
                .append("}")
                .toString();
    }

    private String expectedType(JurisdictionType jurisdictionType) {
        return
                new StringBuilder()
                        .append(MessageType.HEARING.name())
                        .append("-")
                        .append(jurisdictionType.name())
                        .toString();

    }
}
