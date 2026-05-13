package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.id.AsnSeqTxnCaseId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, AsnSeqTxnCaseId> {

    @Query(
            value = "SELECT RS.RESULT_CODE FROM MLA.XXMLA_RESULT RS WHERE RS.case_id =:caseId AND RS.ASN_SEQ = :asnSeq",
            nativeQuery = true)
    List<Integer> findResultCodeByCaseIdAndAsnSeq(Integer caseId, String asnSeq);
}
