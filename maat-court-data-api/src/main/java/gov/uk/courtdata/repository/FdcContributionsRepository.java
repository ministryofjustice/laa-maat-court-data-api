package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FdcContributionsRepository extends JpaRepository<FdcContributionsEntity, Integer> {
    List<FdcContributionsEntity> findByStatus(FdcContributionsStatus status);
    List<FdcContributionsEntity> findByIdIn(Set<Integer> ids);

    @Query(value = "update TOGDATA.FDC_CONTRIBUTIONS fdc SET fdc.status= :status where fdc.id in [:idList]", nativeQuery = true)
    Integer setStatusAndContributionByIdIn(@Param("status") String status, @Param("idList") Set<Integer> ids);

}