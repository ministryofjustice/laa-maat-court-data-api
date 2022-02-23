package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableScheduling
@Getter
@RequiredArgsConstructor
@Slf4j
public class prosecutionConcludedSchedulerService {

    private final ProsecutionConcludedRepository prosecutionConcludedRepository;
    private final ProsecutionConcludedService prosecutionConcludedService;
    private final Gson gson;

    @Scheduled(cron = "0 0/2 * * * *")
    @Transactional
    public void process() {

        log.info("Prosecution Conclusion Scheduling is started");

        List<ProsecutionConcludedEntity> prosecutionConcludedEntityList = prosecutionConcludedRepository.getConcludedCases();
        prosecutionConcludedEntityList.stream()
                .filter(item -> prosecutionConcludedRepository.getPendingCaseConclusions(item.getMaatId()) < 10)
                .map(m -> convertToObject(m.getCaseData()))
                .forEach(this::processConclusion);

        log.info("{} case conclusions are processed - ", prosecutionConcludedEntityList.size());

    }

    private void processConclusion(ProsecutionConcluded prosecutionConcluded) {
        prosecutionConcludedService.execute(prosecutionConcluded);
    }

    private ProsecutionConcluded convertToObject(byte[] caseDate) {

        return gson.fromJson(new String(caseDate, StandardCharsets.UTF_8), ProsecutionConcluded.class);
    }
}
