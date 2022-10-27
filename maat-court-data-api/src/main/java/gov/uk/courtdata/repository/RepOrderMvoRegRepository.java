package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepOrderMvoRegRepository extends JpaRepository<RepOrderMvoRegEntity, Integer>, JpaSpecificationExecutor<RepOrderMvoRegEntity> {

    @Query(value = "SELECT rmvr.* from TOGDATA.rep_orders r\n" +
            "                    left join TOGDATA.rep_order_mvo romv on (romv.rep_id = r.id)\n" +
            "                    left join TOGDATA.rep_order_mvo_reg rmvr on (rmvr.mvo_id = romv.id and romv.vehicle_owner = 'Y')\n" +
            "              where r.id = :repId\n" +
            "              order by rmvr.ID\n" +
            "              ", nativeQuery = true)
    Optional<RepOrderMvoRegEntity> findRepOrderMvoRegByRepId(@Param("repId") Integer repId);

}