package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.id.AsnSeqTxnCaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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


    @Query(value = "SELECT * FROM XXMLA_OFFENCE WHERE CASE_ID = ?1 AND OFFENCE_CODE = ?2 " +
            "AND tx_id = (SELECT max( tx_id ) FROM XXMLA_OFFENCE WHERE CASE_ID = ?1 AND OFFENCE_CODE = ?2)",
            nativeQuery = true)
    Optional<OffenceEntity> findByMaxTxId(Integer caseId, String offenceCode);

}
