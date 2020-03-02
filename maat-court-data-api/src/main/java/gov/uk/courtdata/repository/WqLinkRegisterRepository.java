package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WqLinkRegisterRepository extends JpaRepository<WqLinkRegisterEntity, Integer> {

    @Query(value = "select * from MLA.XXMLA_WQ_LINK_REGISTER wl where wl.MAAT_ID =?1 and wl.REMOVED_TX_ID is null", nativeQuery = true)
    List<WqLinkRegisterEntity> findBymaatId(Integer maatID);
    /**
     *
     * @param maatId
     * @return
     */
    @Query(value = "SELECT count(*)  FROM MLA.XXMLA_WQ_LINK_REGISTER WHERE maat_id  = ?1 and removed_tx_id  is null ", nativeQuery = true)
    Integer getCountByMaatId(final Integer maatId);

    /**
     *
     * @param commonPlatformLibraId
     * @param cjsAreaCode
     * @return
     */
 /*   @Query(value = "SELECT count(*)  FROM MLA.XXMLA_WQ_LINK_REGISTER WHERE libra_id = ?1 and cjs_area_code  = ?2 and removed_tx_id   is null",nativeQuery = true)
    Integer getCountByIdAndCjsCourt(final String commonPlatformLibraId,final String cjsAreaCode);*/
}
