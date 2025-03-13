package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.EformsDecisionHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.helper.ReflectionHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EformsDecisionHistoryService {

    private static final String WROTE_TO_RESULT = "Y";
    private static final String USN_NOT_FOUND = "The USN [%d] not found in Eforms Decision History table";

    private final EformsDecisionHistoryRepository eformsDecisionHistoryRepository;

    @Transactional(readOnly = true)
    public List<EformsDecisionHistory> getAllEformsDecisionHistory(Integer usn) {
        return eformsDecisionHistoryRepository.findAllByUsn(usn);
    }

    @Transactional(readOnly = true)
    public EformsDecisionHistory getNewEformsDecisionHistoryRecord(Integer usn) {
        return eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(usn);
    }

    @Transactional(readOnly = true)
    public EformsDecisionHistory getPreviousEformsDecisionHistoryRecordWroteToResult(Integer usn) {
        return eformsDecisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(usn, WROTE_TO_RESULT).orElse(EformsDecisionHistory.builder().build());
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
    public void updateEformsDecisionHistoryFields(Integer usn, EformsDecisionHistory eformsDecisionHistory) {
        EformsDecisionHistory latestEformsDecisionHistory = Optional.ofNullable(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(usn))
            .orElseThrow(() -> new UsnException(HttpStatus.NOT_FOUND, String.format(USN_NOT_FOUND, usn)));

        ReflectionHelper.updateEntityFromObject(latestEformsDecisionHistory, eformsDecisionHistory);
        eformsDecisionHistoryRepository.save(latestEformsDecisionHistory);
    }
}
