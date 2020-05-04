package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.OffenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OffenceRepository extends JpaRepository<OffenceEntity,Integer> {

    /**
     *  Get offences count for the case id and asn seq.
     * @param caseId
     * @param asnSeq
     * @return
     */
    @Query(value = "SELECT COUNT(*) FROM MLA.XXMLA_OFFENCE WHERE CASE_ID = ?1 AND ASN_SEQ = ?2", nativeQuery = true)
    Integer getOffenceCountForAsnSeq(Integer caseId,String asnSeq);

}
