package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FdcContributionsRepository extends JpaRepository<FdcContributionsEntity, Integer> {
    List<FdcContributionsEntity> findByStatus(FdcContributionsStatus status);
    List<FdcContributionsEntity> findByIdIn(Set<Integer> ids);

    @Modifying
    @Query(nativeQuery = true, value = "MERGE into TOGDATA.fdc_contributions fc " +
            "using    ( " +
            "select f.id from TOGDATA.FDC_CONTRIBUTIONS f where rep_id = 1889713 " +
            "         ) query1         " +
            "ON (fc.id = query1.id) " +
            "WHEN MATCHED THEN " +
            "  UPDATE SET fc.status = fc.status  ")
    List<FdcContributionsEntity> globalUpdate();

}