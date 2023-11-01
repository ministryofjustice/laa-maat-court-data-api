package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.enums.CaseConclusionStatus;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProsecutionConcludedDataService {

    private final ProsecutionConcludedRepository prosecutionConcludedRepository;
    private final Gson gson;

    @Transactional
    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        Integer maatId = prosecutionConcluded.getMaatId();
        log.info("Scheduling MAAT -ID {} for later processing", maatId);

        List<ProsecutionConcludedEntity> prosecutionConcludedEntityList = prosecutionConcludedRepository.getByMaatId(maatId);
        if (prosecutionConcludedEntityList.isEmpty()) {
            ProsecutionConcludedEntity prosecutionConcludedEntity = build(prosecutionConcluded, maatId);
            prosecutionConcludedRepository.save(prosecutionConcludedEntity);
        } else {
            prosecutionConcludedEntityList.forEach(entity -> {
                entity.setRetryCount(entity.getRetryCount() + 1);
                entity.setUpdatedTime(LocalDateTime.now());
            });
            prosecutionConcludedRepository.saveAll(prosecutionConcludedEntityList);
        }
        log.info("MAAT -ID {} scheduling is complete", maatId);
    }

    private ProsecutionConcludedEntity build(ProsecutionConcluded prosecutionConcluded, Integer maatId) {
        return ProsecutionConcludedEntity
                .builder()
                .maatId(maatId)
                .hearingId(prosecutionConcluded.getHearingIdWhereChangeOccurred().toString())
                .caseData(convertAsByte(prosecutionConcluded))
                .status(CaseConclusionStatus.PENDING.name())
                .createdTime(LocalDateTime.now())
                .retryCount(0)
                .updatedTime(LocalDateTime.now())
                .build();
    }

    private byte[] convertAsByte(final ProsecutionConcluded message) {

        return Optional.ofNullable(message).isPresent() && gson.toJson(message) != null ?
                gson.toJson(message).getBytes() : null;
    }

    @Transactional
    public void updateConclusion(Integer maatId) {
        List<ProsecutionConcludedEntity> processedCases = prosecutionConcludedRepository.getByMaatId(maatId);
        processedCases.forEach(concludedCase -> {
            concludedCase.setStatus(CaseConclusionStatus.PROCESSED.name());
            concludedCase.setUpdatedTime(LocalDateTime.now());
        });
        prosecutionConcludedRepository.saveAll(processedCases);
    }


}