package gov.uk.courtdata.contribution.repository;

import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.entity.ContributionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContributionsRepository extends JpaRepository<ContributionsEntity, Integer> {

    Integer countAllByRepOrder_Id(Integer repId);

    List<ContributionsEntity> findAllByRepOrder_Id(Integer repId);

    ContributionsEntity findByRepOrder_IdAndLatestIsTrue(Integer repId);

    @Modifying
    @Query(value = "UPDATE TOGDATA.CONTRIBUTIONS SET REPLACED_DATE = TRUNC(SYSDATE), ACTIVE = 'N' WHERE REP_ID = :repId AND EFFECTIVE_DATE >= :effDate", nativeQuery = true)
    void updateExistingContributionToInactive(@Param("repId") Integer repId, @Param("effDate") LocalDate effDate);

    @Modifying
    @Query(value = "UPDATE TOGDATA.CONTRIBUTIONS SET LATEST = 'N' WHERE REP_ID = :repId", nativeQuery = true)
    void updateExistingContributionToPrior(@Param("repId") Integer repId);

    @Query(value = "SELECT C.ID, C.MONTHLY_CONTRIBS monthlyContributions, C.UPFRONT_CONTRIBS upfrontContributions, " +
            "C.BASED_ON basedOn, C.UPLIFT_APPLIED upliftApplied, C.EFFECTIVE_DATE effectiveDate, " +
            "C.CALC_DATE calcDate FROM TOGDATA.CONTRIBUTIONS C " +
            "WHERE C.REP_ID = :repId ORDER BY C.EFFECTIVE_DATE DESC, C.ID DESC", nativeQuery = true)
    List<ContributionsSummaryView> getContributionsSummary(@Param("repId") Integer repId);
}