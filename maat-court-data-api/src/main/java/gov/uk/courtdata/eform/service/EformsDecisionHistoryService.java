package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.repository.EformsDecisionHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EformsDecisionHistoryService {

    private static final String WROTE_TO_RESULT = "Y";

    private final EformsDecisionHistoryRepository eformsDecisionHistoryRepository;

    @Transactional
    public List<EformsDecisionHistory> getAllEformsDecisionHistory(Integer usn) {
        return eformsDecisionHistoryRepository.findAllByUsn(usn);
    }

    @Transactional
    public EformsDecisionHistory getNewEformsDecisionHistoryRecord(Integer usn) {
        return eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(usn);
    }

    @Transactional
    public EformsDecisionHistory getPreviousEformsDecisionHistoryRecordWroteToResult(Integer usn) {
        return eformsDecisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(usn, WROTE_TO_RESULT);
    }

    @Transactional
    public void createEformsDecisionHistory(EformsDecisionHistory eformsDecisionHistory) {
        eformsDecisionHistoryRepository.saveAndFlush(eformsDecisionHistory);
    }

    @Transactional
    public void deleteEformsDecisionHistory(Integer usn) {
        eformsDecisionHistoryRepository.deleteAllByUsn(usn);
    }

    @Transactional
    public void updateEformsDecisionHistoryFields(Integer usn, Map<String, Object> updateFields) {

        Optional<EformsDecisionHistory> latestEformsDecisionHistory = Optional.ofNullable(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(usn));

        if (latestEformsDecisionHistory.isPresent()) {
            updateFields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(EformsDecisionHistory.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, latestEformsDecisionHistory.get(), value);
            });
            eformsDecisionHistoryRepository.save(latestEformsDecisionHistory.get());
        }
    }
}
