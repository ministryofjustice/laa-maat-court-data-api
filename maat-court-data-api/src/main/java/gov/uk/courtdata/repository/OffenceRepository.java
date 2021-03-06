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


    @Query(value = "SELECT * FROM MLA.XXMLA_OFFENCE WHERE " +
            "CASE_ID = :caseId AND OFFENCE_CODE = :offenceCode AND ASN_SEQ = :asnSeq " +
            "AND TX_ID = (SELECT MAX( TX_ID ) FROM MLA.XXMLA_OFFENCE " +
            "WHERE CASE_ID = :caseId AND OFFENCE_CODE = :offenceCode AND ASN_SEQ = :asnSeq)",
            nativeQuery = true)
    Optional<OffenceEntity> findByMaxTxId(Integer caseId, String offenceCode, String asnSeq);

    List<OffenceEntity> findByCaseId(Integer caseId);

    @Query(value = "SELECT * FROM MLA.XXMLA_OFFENCE WHERE " +
            "CASE_ID = :caseId AND OFFENCE_ID = :offenceId AND APPLICATION_FLAG = :applicationFlag " +
            "AND TX_ID = (SELECT MAX( TX_ID ) FROM MLA.XXMLA_OFFENCE " +
            "WHERE CASE_ID = :caseId AND OFFENCE_ID = :offenceId AND APPLICATION_FLAG = :applicationFlag)",
            nativeQuery = true)
    Optional<OffenceEntity> findApplicationByOffenceCode(Integer caseId, String offenceId, Integer applicationFlag);

}
