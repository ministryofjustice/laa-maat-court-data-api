package gov.uk.courtdata.repository;

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
}