package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.RepOrderMvoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOJAppealRepository extends JpaRepository<IOJAppealEntity, Integer>, JpaSpecificationExecutor<IOJAppealEntity> {

    @Modifying
    @Query(value = "update IOJAppealEntity ioj_ae set ioj_ae.replaced = 'Y' where ioj_ae.repId = :repId and ioj_ae.id <> :iojAppealIDNotToUpdate")
    void setOldIOJAppealsReplaced(@Param("repId") Integer repId, @Param("iojAppealIDNotToUpdate") Integer iojAppealIDNotToUpdate);

    @Query(value = "SELECT ioj_ae FROM IOJAppealEntity ioj_ae WHERE ioj_ae.repId = :repId AND ioj_ae.replaced = 'N'")
    IOJAppealEntity findByRepId(int repId);

    IOJAppealEntity findByRepIdAndReplacedAndDecisionResult(int repId, String replaced, String decisionResult);
}
