package gov.uk.courtdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.repository.QueueMessageLogRepository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueueMessageLogServiceTest {

    @InjectMocks
    public QueueMessageLogService queueMessageLogService;

    @Spy
    public QueueMessageLogRepository queueMessageLogRepository;

    @Captor
    private ArgumentCaptor<QueueMessageLogEntity> queueMessageCaptor;

    @Test
    void whenLinkMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.LINK, newQueueMessage(maatId));

        verify(queueMessageLogRepository, times(1)).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getType()).isNotNull().isEqualTo(MessageType.LINK.name());
            assertThat(savedQueueMsg.getMessage()).isNotNull();
            assertThat(savedQueueMsg.getCreatedTime()).isNotNull();
        });
    }

    @Test
    void whenUnLinkMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.UNLINK, newQueueMessage(maatId));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getType()).isNotNull().isEqualTo(MessageType.UNLINK.name());
            assertThat(savedQueueMsg.getMessage()).isNotNull();
            assertThat(savedQueueMsg.getCreatedTime()).isNotNull();
        });
    }

    @Test
    void whenHearingMessage_CheckLogEntryCreatedForMagsCourt() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(
                MessageType.HEARING, newHearingQueueMessage(maatId, JurisdictionType.MAGISTRATES));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getType()).isNotNull().isEqualTo(expectedType(JurisdictionType.MAGISTRATES));
            assertThat(savedQueueMsg.getMessage()).isNotNull();
            assertThat(savedQueueMsg.getCreatedTime()).isNotNull();
        });
    }

    @Test
    void whenHearingMessage_CheckLogEntryCreatedCrownCourt() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.HEARING, newHearingQueueMessage(maatId, JurisdictionType.CROWN));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getType()).isNotNull().isEqualTo(expectedType(JurisdictionType.CROWN));
            assertThat(savedQueueMsg.getMessage()).isNotNull();
            assertThat(savedQueueMsg.getCreatedTime()).isNotNull();
        });
    }

    @Test
    void whenLaaStatusMessage_CheckLogEntryCreated() {

        final Integer maatId = 1000;

        queueMessageLogService.createLog(MessageType.LAA_STATUS, newQueueMessage(maatId));

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getLaaTransactionId())
                    .isNotNull()
                    .isEqualTo("8720c683-39ef-4168-a8cc-058668a2dcca");
            assertThat(savedQueueMsg.getType()).isNotNull().isEqualTo(MessageType.LAA_STATUS.name());
            assertThat(savedQueueMsg.getMessage()).isNotNull();
            assertThat(savedQueueMsg.getCreatedTime()).isNotNull();
        });
    }

    @Test
    void testWhenMetadataIsNull_thenProcessAsExpected() {

        final Integer maatId = 1000;
        queueMessageLogService.createLog(MessageType.LAA_STATUS, newQueueMessageWithoutMetaData(maatId));
        assertNullLaaTransactionIdIsHandled(maatId);
    }

    @Test
    void testWhenMetadataLaaTransactionIdIsNull_thenProcessAsExpected() {

        final Integer maatId = 1000;
        queueMessageLogService.createLog(
                MessageType.LAA_STATUS, getQueueMessage(maatId, JurisdictionType.MAGISTRATES, null));
        assertNullLaaTransactionIdIsHandled(maatId);
    }

    @Test
    void testWhenMessageTypeIsLaaStatusUpdate_thenProcessAsExpected() {

        String payload =
                """
                        {
                            "data": {
                                "attributes": {
                                    "maat_reference": 324334
                                }
                            }
                        }
                        """;

        queueMessageLogService.createLog(MessageType.LAA_STATUS_UPDATE, payload);

        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(324334);
            assertThat(savedQueueMsg.getLaaTransactionId()).isNull();
            assertThat(savedQueueMsg.getType()).isNotNull();
        });
    }

    private void assertNullLaaTransactionIdIsHandled(Integer maatId) {
        verify(queueMessageLogRepository).save(queueMessageCaptor.capture());

        QueueMessageLogEntity savedQueueMsg = queueMessageCaptor.getValue();

        SoftAssertions.assertSoftly(s -> {
            assertThat(savedQueueMsg).isNotNull();
            assertThat(savedQueueMsg.getMaatId()).isNotNull().isEqualTo(maatId);
            assertThat(savedQueueMsg.getTransactionUUID()).isNotNull();
            assertThat(savedQueueMsg.getLaaTransactionId()).isNull();
            assertThat(savedQueueMsg.getType()).isNotNull();
            assertThat(savedQueueMsg.getMessage()).isNotNull();
        });
    }

    private String newQueueMessage(Integer maatId) {

        return new StringBuilder()
                .append("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":")
                .append(maatId)
                .append("}")
                .toString();
    }

    private String newQueueMessageWithoutMetaData(Integer maatId) {

        return "{" + "    \"maatId\": " + maatId + "\n" + "}";
    }

    private String getQueueMessage(Integer maatId, JurisdictionType jurisdictionType, String laaTransactionId) {

        return "{" + "    \"maatId\": "
                + maatId + ",\n" + "    \"jurisdictionType\": "
                + jurisdictionType.name() + ",\n" + "    \"metadata\": {\n"
                + "        \"laaTransactionId\": "
                + laaTransactionId + "\n" + "    }\n"
                + "}";
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
        return new StringBuilder()
                .append(MessageType.HEARING.name())
                .append("-")
                .append(jurisdictionType.name())
                .toString();
    }
}
