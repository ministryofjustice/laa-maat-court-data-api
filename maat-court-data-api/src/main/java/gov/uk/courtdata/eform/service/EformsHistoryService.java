package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.repository.EformsHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EformsHistoryService {

   private final EformsHistoryRepository eformsHistoryRepository;

    @Transactional(readOnly = true)
    public List<EformsHistory> getAllEformsHistory(Integer usn) {
        return eformsHistoryRepository.findAllByUsn(usn);
    }
    @Transactional(readOnly = true)
    public EformsHistory getLatestEformsHistoryRecord(Integer usn) {
        return eformsHistoryRepository.findTopByUsnOrderByIdDesc(usn);
    }

    @Transactional
    public void createEformsHistory(EformsHistory eformsHistory) {
        eformsHistoryRepository.saveAndFlush(eformsHistory);
    }

    @Transactional
    public void deleteEformsHistory(Integer usn) {
        eformsHistoryRepository.deleteAllByUsn(usn);
    }
}
