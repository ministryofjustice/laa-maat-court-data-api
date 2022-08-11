package gov.uk.courtdata.util;

import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import org.assertj.core.api.SoftAssertions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
            assertThat(loggedMessages.size()).isEqualTo(expectedNumberOfMessages);
            QueueMessageLogEntity messageLog = loggedMessages.get(0);
            assertThat(messageLog.getMessage()).isEqualTo(messageBlobBytes);
            assertThat(messageLog.getLaaTransactionId()).isEqualTo(laaTransactionId);
            assertThat(messageLog.getMaatId()).isEqualTo(maatId);
        });
    }

}
