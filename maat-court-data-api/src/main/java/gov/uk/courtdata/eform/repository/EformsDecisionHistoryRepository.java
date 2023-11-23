package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EformsDecisionHistoryRepository extends JpaRepository<EformsDecisionHistory, Integer> {
    List<EformsDecisionHistory> findAllByUsn(Integer usn);
    void deleteAllByUsn(Integer usn);
    EformsDecisionHistory findTopByUsnOrderByIdDesc(Integer usn);
    EformsDecisionHistory findFirstByUsnAndWroteToResultsOrderByIdDesc(Integer usn, String wroteResult);
}
