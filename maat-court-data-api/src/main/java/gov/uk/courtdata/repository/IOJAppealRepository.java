package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.IOJAppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOJAppealRepository extends JpaRepository<IOJAppealEntity, Integer> {

    @Modifying
    @Query(value = "update IOJAppealEntity ioj_ae set ioj_ae.replaced = 'Y' where ioj_ae.repId = :repId and ioj_ae.id <> :iojAppealIDNotToUpdate")
    void setOldIOJAppealsReplaced(@Param("repId") Integer repId, @Param("iojAppealIDNotToUpdate") Integer iojAppealIDNotToUpdate);

    Optional<IOJAppealEntity> findByRepId(int repId);
}
