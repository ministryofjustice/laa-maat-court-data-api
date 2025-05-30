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

    /***
     * This method will return the FDC_CALCULATION_DELAY that has been set in the MAAT DB.
     * This value is used to determine the date range for both accelerated and delayed FDCs.
     * @return
     */
    @Query(value = "SELECT TOGDATA.REF_DATA_MANAGEMENT.GET_CONFIG_PARAMETER('FDC_CALCULATION_DELAY', sysdate) FROM DUAL", nativeQuery = true)
    String callGetFdcCalculationDelay();

    @Modifying
    @Query("""
           UPDATE FdcContributionsEntity f
               SET f.status = :newStatus,
               f.userModified = :userModified,
               f.dateModified = CURRENT_DATE
               WHERE f.repOrderEntity.id = :repId
               AND f.status = :previousStatus""")
    Integer updateStatus(@Param("repId") Integer repId,
                         @Param("userModified") String userModified,
                         @Param("newStatus") FdcContributionsStatus newStatus,
                         @Param("previousStatus") FdcContributionsStatus previousStatus);

    @Modifying
    @Query("""
           UPDATE FdcContributionsEntity f
               SET f.status = :newStatus,
               f.userModified = :userModified,
               f.dateModified = CURRENT_DATE
               WHERE f.repOrderEntity.id = :repId""")
    Integer updateStatusByRepId(@Param("repId") Integer repId,
                                @Param("userModified") String userModified,
                                @Param("newStatus") FdcContributionsStatus newStatus);
}