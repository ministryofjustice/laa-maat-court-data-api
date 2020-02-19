package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WqLinkRegisterRepository extends JpaRepository<WqLinkRegisterEntity, Integer> {

    @Query(value = "select * from XXMLA_WQ_LINK_REGISTER wl where wl.MAAT_ID =?1 and wl.REMOVED_TX_ID is null", nativeQuery = true)
    List<WqLinkRegisterEntity> findBymaatId(Integer maatID);
}
