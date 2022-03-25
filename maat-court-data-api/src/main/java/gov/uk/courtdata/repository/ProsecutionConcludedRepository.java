package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProsecutionConcludedRepository extends JpaRepository<ProsecutionConcludedEntity, Integer> {

    @Query(value = "SELECT * FROM MLA.XXMLA_PROSECUTION_CONCLUDED where STATUS = 'PENDING'", nativeQuery = true)
    List<ProsecutionConcludedEntity> getConcludedCases();

    List<ProsecutionConcludedEntity> getByMaatId(Integer maatId);

    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_PROSECUTION_CONCLUDED where maat_id  = ?1 AND STATUS = 'PENDING'", nativeQuery = true)
    Integer getPendingCaseConclusions(final int maatId);

    List<ProsecutionConcludedEntity> getByHearingId(String hearingId);
}
