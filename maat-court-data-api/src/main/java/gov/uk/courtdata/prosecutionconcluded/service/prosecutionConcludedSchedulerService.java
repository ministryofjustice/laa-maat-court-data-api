package gov.uk.courtdata.prosecutionconcluded.service;

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
@RequiredArgsConstructor
@Slf4j
public class prosecutionConcludedSchedulerService {

    private final ProsecutionConcludedRepository prosecutionConcludedRepository;
    private final ProsecutionConcludedService prosecutionConcludedService;
    private final WQHearingRepository wqHearingRepository;
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
        WQHearingEntity wqHearingEntity = getWqHearingEntity(data.getMaatId(), data.getHearingId());
        return wqHearingEntity != null
                && JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType());
    }

    private WQHearingEntity getWqHearingEntity(Integer maatId, String hearingId) {
        List<WQHearingEntity> wqHearingEntityList = wqHearingRepository
                .findByMaatIdAndHearingUUID(maatId, hearingId);
        return !wqHearingEntityList.isEmpty() ? wqHearingEntityList.get(0) : null;
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
        WQHearingEntity wqHearingEntity = getWqHearingEntity(concludedData.getMaatId(), concludedData.getHearingId());
        return wqHearingEntity != null
                && JurisdictionType.MAGISTRATES.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType());
    }
}
