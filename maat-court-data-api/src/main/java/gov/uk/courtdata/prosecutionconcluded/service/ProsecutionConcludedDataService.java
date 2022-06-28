package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
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

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class ProsecutionConcludedDataService {

    private final ProsecutionConcludedRepository prosecutionConcludedRepository;
    private final Gson gson;

    @Transactional
    public void execute(final ProsecutionConcluded prosecutionConcluded) {
        log.info("Scheduling MAAT -ID {} for later processing", prosecutionConcluded.getMaatId());
        ProsecutionConcludedEntity prosecutionConcludedEntity = ProsecutionConcludedEntity
                .builder()
                .maatId(prosecutionConcluded.getMaatId())
                .hearingId(prosecutionConcluded.getHearingIdWhereChangeOccurred().toString())
                .caseData(convertAsByte(prosecutionConcluded))
                .status(CaseConclusionStatus.PENDING.name())
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();

        prosecutionConcludedRepository.save(prosecutionConcludedEntity);
        log.info("MAAT -ID {} scheduling is complete", prosecutionConcluded.getMaatId());
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
            concludedCase.setCreatedTime(LocalDateTime.now());
        });
        prosecutionConcludedRepository.saveAll(processedCases);
    }


}