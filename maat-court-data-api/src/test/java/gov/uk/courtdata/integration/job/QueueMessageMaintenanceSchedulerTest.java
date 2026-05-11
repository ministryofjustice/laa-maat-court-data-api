package gov.uk.courtdata.integration.job;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.job.QueueMessageMaintenanceScheduler;
import gov.uk.courtdata.repository.QueueMessageLogRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestPropertySource(locations = {"classpath:application.yaml"})
class QueueMessageMaintenanceSchedulerTest extends MockMvcIntegrationTest {

    @Value("${queue.message.log.cron.expression}")
    private String cronExpression;

    @Autowired
    private QueueMessageMaintenanceScheduler messageMaintenanceJob;

    private static byte[] buildMessage(Integer maatId) {
        return ("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":" + maatId + "}").getBytes();
    }

    @Test
    void verifyExpiryInDays_IsSet() {
        SoftAssertions.assertSoftly(s -> {
            assertThat(messageMaintenanceJob.getExpiryInDays()).isNotNull();
            assertThat(messageMaintenanceJob.getExpiryInDays()).isEqualTo(15);
        });
    }

    @Test
    void verifyCronJob_IsScheduledToRun_EveryDay_Midnight() {
        final String expr = "0 0 0 * * *";
        SoftAssertions.assertSoftly(s -> {
            assertThat(cronExpression).isNotNull();
            assertThat(cronExpression).as("Cron expressions don't match.").isEqualTo(expr);
        });
    }

    @Test
    void whenScheduledJobRuns_MessageLogBeforeExpiryDays_NotPurged() {

        getQueueMessageLogRepository()
                .save(QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .maatId(1000)
                        .createdTime(LocalDateTime.now())
                        .message(buildMessage(1000))
                        .build());

        getQueueMessageLogRepository()
                .save(QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .maatId(1001)
                        .createdTime(LocalDateTime.now().minusDays(2))
                        .message(buildMessage(1001))
                        .build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageList = getQueueMessageLogRepository().findAll();

        SoftAssertions.assertSoftly(s -> {
            assertThat(messageList).isNotNull();
            assertThat(messageList).hasSize(2);
        });
    }

    @Test
    void whenScheduledJobRuns_MessageLoggedOnOrAfterExpiry_IsPurged() {

        getQueueMessageLogRepository()
                .save(QueueMessageLogEntity.builder()
                        .transactionUUID(UUID.randomUUID().toString())
                        .maatId(1111)
                        .createdTime(LocalDateTime.now()
                                .minusDays(messageMaintenanceJob.getExpiryInDays())
                                .minusMinutes(1))
                        .message(buildMessage(1111))
                        .build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageLogEntities =
                getQueueMessageLogRepository().findAll();

        SoftAssertions.assertSoftly(s -> {
            assertThat(messageLogEntities).isNotNull();
            assertThat(messageLogEntities).isEmpty();
        });
    }

    private QueueMessageLogRepository getQueueMessageLogRepository() {
        return messageMaintenanceJob.getQueueMessageLogRepository();
    }
}
