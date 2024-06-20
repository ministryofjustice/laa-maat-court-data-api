package gov.uk.courtdata.integration.job;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.job.QueueMessageMaintenanceScheduler;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestPropertySource(locations = {"classpath:application.yaml"})
public class QueueMessageMaintenanceSchedulerTest  extends MockMvcIntegrationTest {

    @Value("${queue.message.log.cron.expression}")
    private String cronExpression;

    @Autowired
    private QueueMessageMaintenanceScheduler messageMaintenanceJob;


    private static byte[] buildMessage(Integer maatId) {
        return ("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":" + maatId + "}").getBytes();
    }

    @Test
    public void verifyExpiryInDays_IsSet() {

        assertAll("expiryInDays",
                () -> assertNotNull(messageMaintenanceJob.getExpiryInDays()),
                () -> assertEquals(15, messageMaintenanceJob.getExpiryInDays()));
    }

    @Test
    public void verifyCronJob_IsScheduledToRun_EveryDay_Midnight() {

        final String expr = "0 0 0 * * *";

        assertAll("verifyCronExpr",
                () -> assertNotNull(cronExpression),
                () -> assertEquals(expr, cronExpression, "Cron expressions don't match."));

    }

    @Test
    public void whenScheduledJobRuns_MessageLogBeforeExpiryDays_NotPurged() {

        getQueueMessageLogRepository().save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1000)
                .createdTime(LocalDateTime.now())
                .message(buildMessage(1000)).build());

        getQueueMessageLogRepository().save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1001)
                .createdTime(LocalDateTime.now().minusDays(2))
                .message(buildMessage(1001)).build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageList = getQueueMessageLogRepository().findAll();

        assertAll("messagelist",
                () -> assertNotNull(messageList),
                () -> assertEquals(2, messageList.size()));
    }

    @Test
    public void whenScheduledJobRuns_MessageLoggedOnOrAfterExpiry_IsPurged() {

        getQueueMessageLogRepository().save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1111)
                .createdTime(LocalDateTime.now().minusDays(messageMaintenanceJob.getExpiryInDays())
                        .minusMinutes(1))
                .message(buildMessage(1111)).build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageLogEntities = getQueueMessageLogRepository().findAll();

        assertAll("messageLogEntities",
                () -> assertNotNull(messageLogEntities),
                () -> assertTrue(messageLogEntities.isEmpty()));
    }

    private QueueMessageLogRepository getQueueMessageLogRepository() {
        return messageMaintenanceJob.getQueueMessageLogRepository();
    }

}
