package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.XLATResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XLATResultRepository extends JpaRepository<XLATResultEntity,Integer> {

    @Query("FROM XLATResultEntity r WHERE r.ccImprisonment='Y'")
    List<XLATResultEntity> fetchResultCodesForCCImprisonment();

    @Query("FROM XLATResultEntity r WHERE r.ccBenchWarrant='Y'")
    List<XLATResultEntity> findByCjsResultCodeIn();

    List<XLATResultEntity> findByWqType(Integer wqType);

    @Query(value = "SELECT RS.CJS_RESULT_CODE FROM MLA.XXMLA_XLAT_RESULT RS " +
            "WHERE RS.WQ_TYPE = :wqType AND RS.SUBTYPE_CODE = :subTypeCode",
            nativeQuery = true)
    List<Integer> findResultsByWQType(int wqType,int subTypeCode);
}
