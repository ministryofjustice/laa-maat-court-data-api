package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WQResultEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WQResultRepository extends JpaRepository<WQResultEntity, Integer> {

    @Query(
            value =
                    "SELECT RS.RESULT_CODE FROM MLA.XXMLA_WQ_RESULT RS WHERE RS.case_id = :caseId AND RS.ASN_SEQ = :asnSeq",
            nativeQuery = true)
    List<Integer> findResultCodeByCaseIdAndAsnSeq(Integer caseId, String asnSeq);
}
