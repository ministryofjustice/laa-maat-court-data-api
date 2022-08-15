package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RepOrderRepository extends JpaRepository<RepOrderEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE REP_ORDERS SET ass_date_completed = :dateCompleted WHERE id = :repId", nativeQuery = true)
    void updateAppDateCompleted(@Param("repId") Integer repId, @Param("dateCompleted") LocalDateTime dateCompleted);

}
