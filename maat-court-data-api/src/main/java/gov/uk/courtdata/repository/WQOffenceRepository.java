package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQOffenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WQOffenceRepository extends JpaRepository<WQOffenceEntity,Integer> {

    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_WQ_OFFENCE WHERE CASE_ID = ?1 AND OFFENCE_ID = ?2 AND CC_NEW_OFFENCE = 'Y'", nativeQuery = true)
    Integer getNewOffenceCount(Integer caseId, String offenceId);
}
