package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.id.AsnSeqTxnCaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OffenceRepository extends JpaRepository<OffenceEntity, AsnSeqTxnCaseId> {

    /**
     * Get offences count for the case id and asn seq.
     *
     * @param caseId
     * @param asnSeq
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_OFFENCE WHERE CASE_ID = ?1 AND ASN_SEQ = ?2", nativeQuery = true)
    Integer getOffenceCountForAsnSeq(Integer caseId, String asnSeq);

    @Query(value =
            "SELECT * FROM" +
                    "(SELECT * FROM" +
                    "(SELECT * FROM MLA.XXMLA_OFFENCE WHERE CASE_ID = :caseId AND ASN_SEQ = :asnSeq)" +
                    "WHERE OFFENCE_CODE = :offenceCode ORDER BY TX_ID DESC)" +
                    "WHERE ROWNUM < 2", nativeQuery = true)
    Optional<OffenceEntity> findByMaxTxId(Integer caseId, String offenceCode, String asnSeq);

    List<OffenceEntity> findByCaseId(Integer caseId);

    @Query(value =
            "SELECT * FROM" +
                    "(SELECT * FROM" +
                    "(SELECT * FROM MLA.XXMLA_OFFENCE WHERE CASE_ID = :caseId AND APPLICATION_FLAG = :applicationFlag)" +
                    "WHERE OFFENCE_ID = :offenceId ORDER BY TX_ID DESC)" +
                    "WHERE ROWNUM < 2", nativeQuery = true)
    Optional<OffenceEntity> findApplicationByOffenceCode(Integer caseId, String offenceId, Integer applicationFlag);

    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_OFFENCE WHERE CASE_ID = ?1 AND OFFENCE_ID = ?2 AND CC_NEW_OFFENCE = 'Y'", nativeQuery = true)
    Integer getNewOffenceCount(Integer caseId, String offenceId);

}
