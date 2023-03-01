package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.RepOrderCapitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepOrderCapitalRepository extends JpaRepository<RepOrderCapitalEntity, Integer> {


    @Query(value = "SELECT COUNT(*) FROM TOGDATA.rep_order_capital WHERE rep_id =:repId AND capt_capital_type <> 'PROPERTY' " +
            "AND active = 'Y' AND date_all_evidence_received IS NOT NULL", nativeQuery = true)
    Integer getCapitalAssetCount(@Param("repId") Integer repId);
}
