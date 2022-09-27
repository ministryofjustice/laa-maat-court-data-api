package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.CaseConclusionStatus;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@Getter
@XRayEnabled
@Slf4j
@RequiredArgsConstructor
public class prosecutionConcludedScheduler {

    private final ProsecutionConcludedRepository prosecutionConcludedRepository;
    private final ProsecutionConcludedService prosecutionConcludedService;
    private final HearingsService hearingsService;
    private final Gson gson;

    @Scheduled(cron = "${queue.message.log.cron.expression}")
    public void process() {

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

    }

    private void processCaseConclusion(ProsecutionConcluded prosecutionConcluded) {
        try {
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
