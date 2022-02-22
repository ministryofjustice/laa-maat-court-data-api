package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.QueueMessageLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface QueueMessageLogRepository extends JpaRepository<QueueMessageLogEntity, Integer> {

    @Modifying
    @Query("delete from QueueMessageLogEntity q where q.createdTime<=:expiryDate")
    void deleteCreatedOnOrBefore(@Param("expiryDate") LocalDateTime expiryDate);

    @Query("Select count(qml.maatId) from QueueMessageLogEntity qml where qml.maatId=:maatId and qml.type='PROSECUTION_CONCLUDED'")
    int getMessageCounterByMaatId(@Param("maatId") Integer maatId);

}
