package gov.uk.courtdata.integrationTest.job;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.QueueMessageLogEntity;
import gov.uk.courtdata.integrationTest.MockServicesConfig;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@TestPropertySource(locations = {"classpath:application.yaml"})
public class QueueMessageMaintenanceJobTest {

    @Value("${queue.message.log.cron.expression}")
    private String cronExpression;

    @Autowired
    private QueueMessageMaintenanceJob messageMaintenanceJob;

    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;

    private static byte[] buildMessage(Integer maatId) {

        return new StringBuilder()
                .append("{laaTransactionId:\"8720c683-39ef-4168-a8cc-058668a2dcca\",\"maatId\":")
                .append(maatId)
                .append("}")
                .toString()
                .getBytes();
    }

    @Before
    public void setUp() {
        queueMessageLogRepository.deleteAll();
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
                () -> assertTrue(cronExpression.equals(expr), "Cron expressions don't match."));

    }

    @Test
    public void whenScheduledJobRuns_MessageLogBeforeExpiryDays_NotPurged() {

        queueMessageLogRepository.save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1000)
                .createdTime(LocalDateTime.now())
                .message(buildMessage(1000)).build());

        queueMessageLogRepository.save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1001)
                .createdTime(LocalDateTime.now().minusDays(2))
                .message(buildMessage(1001)).build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageList = queueMessageLogRepository.findAll();

        assertAll("messagelist",
                () -> assertNotNull(messageList),
                () -> assertEquals(2, messageList.size()));
    }

    @Test
    public void whenScheduledJobRuns_MessageLoggedOnOrAfterExpiry_IsPurged() {

        queueMessageLogRepository.save(QueueMessageLogEntity.builder()
                .transactionUUID(UUID.randomUUID().toString())
                .maatId(1111)
                .createdTime(LocalDateTime.now().minusDays(messageMaintenanceJob.getExpiryInDays())
                        .minusMinutes(1))
                .message(buildMessage(1111)).build());

        messageMaintenanceJob.purgeHistory();

        List<QueueMessageLogEntity> messageLogEntities = queueMessageLogRepository.findAll();

        assertAll("messageLogEntities",
                () -> assertNotNull(messageLogEntities),
                () -> assertTrue(messageLogEntities.isEmpty()));
    }

    @After
    public void tearDown() {
        queueMessageLogRepository.deleteAll();
    }

}
