package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.CaseConclusionStatus;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
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

        processMAGSCaseConclusion();
        processCCCaseConclusion();


        log.info("Case conclusions are processed");

    }

    private void processCCCaseConclusion() {
        List<ProsecutionConcludedEntity> prosecutionConcludedEntityList = prosecutionConcludedRepository.getConcludedCases();
        prosecutionConcludedEntityList
                .stream()
                .filter(this::isCCConclusion)
                .collect(Collectors
                        .toMap(ProsecutionConcludedEntity::getMaatId, ProsecutionConcludedEntity::getCaseData, (a1, a2) -> a1))
                .entrySet()
                .stream()
                .filter(item -> prosecutionConcludedRepository.getPendingCaseConclusions(item.getKey()) < 10)
                .map(m -> convertToObject(m.getValue()))
                .forEach(this::processConclusion);
    }

    private boolean isCCConclusion(ProsecutionConcludedEntity data) {
        WQHearingEntity wqHearingEntity =
                hearingsService.retrieveHearingForCaseConclusion(convertToObject(data.getCaseData()));
        return wqHearingEntity != null
                && JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType());
    }

    private void processConclusion(ProsecutionConcluded prosecutionConcluded) {
        prosecutionConcludedService.execute(prosecutionConcluded);
    }

    private ProsecutionConcluded convertToObject(byte[] caseDate) {

        return gson.fromJson(new String(caseDate, StandardCharsets.UTF_8), ProsecutionConcluded.class);
    }

    private void processMAGSCaseConclusion() {
        List<ProsecutionConcludedEntity> prosecutionConcludedEntityList = prosecutionConcludedRepository.getConcludedCases();
        prosecutionConcludedEntityList
                .stream()
                .filter(this::isMAGSConclusion)
                .forEach(a -> updateConclusion(a.getHearingId()));
    }

    @Transactional
    public void updateConclusion(String hearingId) {
        List<ProsecutionConcludedEntity> processedCases = prosecutionConcludedRepository.getByHearingId(hearingId);
        processedCases.forEach(concludedCase -> {
            concludedCase.setStatus(CaseConclusionStatus.PROCESSED.name());
            concludedCase.setCreatedTime(LocalDateTime.now());
        });
        prosecutionConcludedRepository.saveAll(processedCases);
    }

    private boolean isMAGSConclusion(ProsecutionConcludedEntity concludedData) {
        WQHearingEntity wqHearingEntity =
                hearingsService.retrieveHearingForCaseConclusion(convertToObject(concludedData.getCaseData()));
        return wqHearingEntity != null
                && JurisdictionType.MAGISTRATES.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType());
    }
}
