package gov.uk.courtdata.integrationTest.job;


import gov.uk.courtdata.repository.QueueMessageLogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@Slf4j
@Getter
@RequiredArgsConstructor
public class QueueMessageMaintenanceJob {


    private final QueueMessageLogRepository queueMessageLogRepository;

    @Value("${queue.message.log.expiryInDays}")
    private Integer expiryInDays;

    @Scheduled(cron = "${queue.message.log.cron.expression}")
    @Transactional
    public void purgeHistory() {

        log.info("Start Queue Message Purge Job...");

        LocalDateTime currentDate = LocalDateTime.now();

        queueMessageLogRepository.deleteCreatedOnOrBefore(currentDate.minusDays(getExpiryInDays()));

        log.info("Finish Queue Message Purge Job...");
    }
}
