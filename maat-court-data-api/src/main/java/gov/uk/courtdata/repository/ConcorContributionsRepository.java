package gov.uk.courtdata.repository;

import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface ConcorContributionsRepository extends JpaRepository<ConcorContributionsEntity, Integer> {
    List<ConcorContributionsEntity> findByStatus(ConcorContributionStatus status);
    List<ConcorContributionsEntity> findByIdIn(Set<Integer> ids);

    @Query("""
       SELECT cc.id
       FROM ConcorContributionsEntity cc
       WHERE cc.status = :status
       AND cc.id > :startId
       ORDER BY cc.id ASC""")
    List<Integer> findByStatusAndIdGreaterThan(
            @Param("status") ConcorContributionStatus status,
            @Param("startId") Integer startId,
            Pageable pageable);


    @Query("""
           SELECT cc.id
               FROM ConcorContributionsEntity cc
               WHERE cc.status = 'SENT'
               AND cc.fullXml IS NOT NULL
               ORDER BY cc.id DESC""")
    List<Integer> findIdsForUpdate(Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
           UPDATE ConcorContributionsEntity cc
               SET cc.status = :newStatus,
               cc.contribFileId = NULL,
               cc.dateModified = CURRENT_DATE,
               cc.userModified = :userModified
               WHERE cc.id IN :ids""")
    int updateStatusAndResetContribFileForIds(@Param("newStatus") ConcorContributionStatus newStatus,
                                              @Param("userModified") String userModified,
                                              @Param("ids") List<Integer> ids);
}