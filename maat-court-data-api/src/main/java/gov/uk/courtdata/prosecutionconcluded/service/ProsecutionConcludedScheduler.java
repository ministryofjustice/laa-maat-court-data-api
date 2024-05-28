package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.CaseConclusionStatus;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(value = "feature.prosecution-concluded-schedule.enabled", havingValue = "true")
public class ProsecutionConcludedScheduler {

    private final Gson gson;
    private final HearingsService hearingsService;
    private final ProsecutionConcludedService prosecutionConcludedService;
    private final ProsecutionConcludedRepository prosecutionConcludedRepository;

    @Scheduled(cron = "${queue.message.log.cron.expression}")
    public void process() {
        String laaTransactionId = UUID.randomUUID().toString();
        LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionId);
        log.info("Prosecution Conclusion Scheduling is started");

        prosecutionConcludedRepository.getConcludedCases()
                .stream()
                .collect(Collectors
                        .toMap(ProsecutionConcludedEntity::getMaatId, ProsecutionConcludedEntity::getCaseData, (a1, a2) -> a1))
                .values()
                .stream()
                .map(this::convertToObject)
                .forEach(this::processCaseConclusion);

        log.info("Case conclusions are processed");
        LoggingData.LAA_TRANSACTION_ID.putInMDC("");
    }

    private void processCaseConclusion(ProsecutionConcluded prosecutionConcluded) {
        try {
            LoggingData.MAATID.putInMDC(prosecutionConcluded.getMaatId());
            WQHearingEntity hearingEntity = hearingsService.retrieveHearingForCaseConclusion(prosecutionConcluded);
            if (hearingEntity != null) {
                if (isCCConclusion(hearingEntity)) {
                    prosecutionConcludedService.executeCCOutCome(prosecutionConcluded,hearingEntity);
                } else {
                    updateConclusion(prosecutionConcluded.getHearingIdWhereChangeOccurred().toString(),CaseConclusionStatus.PROCESSED);
                }
            }
        } catch (Exception exception){
            log.error("Prosecution Conclusion failed for MAAT ID :" + prosecutionConcluded.getMaatId());
            updateConclusion(prosecutionConcluded.getHearingIdWhereChangeOccurred().toString(),CaseConclusionStatus.ERROR);

        }
        LoggingData.MAATID.putInMDC("-");
    }

    private boolean isCCConclusion(WQHearingEntity wqHearingEntity) {

        return JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType());
    }

    private ProsecutionConcluded convertToObject(byte[] caseDate) {
        return gson.fromJson(new String(caseDate, StandardCharsets.UTF_8), ProsecutionConcluded.class);
    }


    @Transactional
    public void updateConclusion(String hearingId, CaseConclusionStatus caseConclusionStatus) {
        List<ProsecutionConcludedEntity> processedCases = prosecutionConcludedRepository.getByHearingId(hearingId);
        processedCases.forEach(concludedCase -> {
            concludedCase.setStatus(caseConclusionStatus.name());
            concludedCase.setUpdatedTime(LocalDateTime.now());
        });
        prosecutionConcludedRepository.saveAll(processedCases);
    }


}
