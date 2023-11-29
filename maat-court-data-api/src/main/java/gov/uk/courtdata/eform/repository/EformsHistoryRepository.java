package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EformsHistoryRepository extends JpaRepository<EformsHistory, Integer> {

    List<EformsHistory> findAllByUsn(Integer usn);
    EformsHistory findTopByUsnOrderByIdDesc(Integer usn);
    void deleteAllByUsn(Integer usn);

}