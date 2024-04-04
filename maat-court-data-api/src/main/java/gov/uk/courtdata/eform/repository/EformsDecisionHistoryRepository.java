package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EformsDecisionHistoryRepository extends JpaRepository<EformsDecisionHistory, Integer> {
    List<EformsDecisionHistory> findAllByUsn(Integer usn);
    void deleteAllByUsn(Integer usn);
    EformsDecisionHistory findTopByUsnOrderByIdDesc(Integer usn);

    @Override
    Optional<EformsDecisionHistory> findById(Integer integer);

    Optional <EformsDecisionHistory> findFirstByUsnAndWroteToResultsOrderByIdDesc(Integer usn, String wroteResult);
}
