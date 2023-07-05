package gov.uk.courtdata.contribution.repository;

import gov.uk.courtdata.contribution.entity.ContributionsSummaryEntity;
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

    Integer countAllByRepId(Integer repId);

    List<ContributionsEntity> findAllByRepId(Integer repId);

    ContributionsEntity findByRepIdAndLatestIsTrue(Integer repId);

    @Modifying
    @Query(value = "UPDATE TOGDATA.CONTRIBUTIONS SET REPLACED_DATE = TRUNC(SYSDATE), ACTIVE = 'N' WHERE REP_ID = :repId AND EFFECTIVE_DATE >= :effDate", nativeQuery = true)
    void updateExistingContributionToInactive(@Param("repId") Integer repId, @Param("effDate") LocalDate effDate);

    @Modifying
    @Query(value = "UPDATE TOGDATA.CONTRIBUTIONS SET LATEST = 'N' WHERE REP_ID = :repId", nativeQuery = true)
    void updateExistingContributionToPrior(@Param("repId") Integer repId);

    @Query(value = "SELECT count(*) from TOGDATA.CONTRIBUTIONS c join TOGDATA.CORRESPONDENCE co on ( CO.ID = C.CORR_ID ) " +
            "where C.REP_ID = :repId and (  CO.COTY_CORRESPONDENCE_TYPE = 'CONTRIBUTION_ORDER' or" +
            " CO.COTY_CORRESPONDENCE_TYPE = 'CONTRIBUTION_NOTICE')", nativeQuery = true)
    int getContributionCount(@Param("repId") Integer repId);

    @Query(value = "SELECT C.ID, C.MONTHLY_CONTRIBS, C.UPFRONT_CONTRIBS, C.BASED_ON, C.UPLIFT_APPLIED, " +
            "C.EFFECTIVE_DATE, C.CALC_DATE, F.FILE_NAME, F.DATE_SENT, F.DATE_RECEIVED " +
            "FROM TOGDATA.CONTRIBUTIONS C LEFT JOIN TOGDATA.CONTRIBUTION_FILES F ON (F.ID = C.CONT_FILE_ID) " +
            "WHERE C.REP_ID = :repId ORDER BY C.EFFECTIVE_DATE DESC, C.ID DESC", nativeQuery = true)
    List<ContributionsSummaryEntity> getContributionsSummary(@Param("repId") Integer repId);
}