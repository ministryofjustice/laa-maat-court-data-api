package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.id.CaseTxnId;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<CaseEntity, CaseTxnId> {

    @Query(
            value =
                    """
                     SELECT * FROM MLA.XXMLA_CASE
                     WHERE CASE_ID = ?1
                     AND TX_ID =?2
                     AND ROWNUM = 1
                  """,
            nativeQuery = true)
    Optional<CaseEntity> getCaseDetail(Integer caseId, Integer txId);
}
