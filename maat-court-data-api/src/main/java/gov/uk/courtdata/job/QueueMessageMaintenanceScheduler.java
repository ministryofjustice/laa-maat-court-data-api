package gov.uk.courtdata.job;


import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class QueueMessageMaintenanceScheduler {


    private final QueueMessageLogRepository queueMessageLogRepository;

    @Value("${queue.message.log.expiryInDays}")
    private Integer expiryInDays;

    @Scheduled(cron = "${queue.message.log.cron.expression}")
    @Transactional
    public void purgeHistory() {
        String laaTransactionId = UUID.randomUUID().toString();
        LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionId);
        log.info("Start Queue Message Purge Job...");

        LocalDateTime currentDate = LocalDateTime.now();

        queueMessageLogRepository.deleteCreatedOnOrBefore(currentDate.minusDays(getExpiryInDays()));

        log.info("Finish Queue Message Purge Job...");
        LoggingData.LAA_TRANSACTION_ID.removeFromMdc(LoggingData.LAA_TRANSACTION_ID.getKey());
    }
}
