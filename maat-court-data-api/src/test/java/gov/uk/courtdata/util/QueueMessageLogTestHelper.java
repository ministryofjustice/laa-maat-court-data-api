package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.repository.QueueMessageLogRepository;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageLogTestHelper {

    private final QueueMessageLogRepository queueMessageLogRepository;

    public QueueMessageLogTestHelper(QueueMessageLogRepository queueMessageLogRepository) {
        this.queueMessageLogRepository = queueMessageLogRepository;
    }

    public void assertQueueMessageLogged(
            String expectedMessageBlob, int expectedNumberOfMessages, String laaTransactionId, Integer maatId) {
        byte[] messageBlobBytes = expectedMessageBlob.getBytes();
        List<QueueMessageLogEntity> loggedMessages = queueMessageLogRepository.findAll();
        SoftAssertions.assertSoftly(softly -> {
            assertThat(loggedMessages).hasSize(expectedNumberOfMessages);
            QueueMessageLogEntity messageLog = loggedMessages.getFirst();
            assertThat(messageLog.getMessage()).isEqualTo(messageBlobBytes);
            assertThat(messageLog.getLaaTransactionId()).isEqualTo(laaTransactionId);
            assertThat(messageLog.getMaatId()).isEqualTo(maatId);
        });
    }
}
