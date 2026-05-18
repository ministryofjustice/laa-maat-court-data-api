package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WqLinkRegisterRepository extends JpaRepository<WqLinkRegisterEntity, Integer> {

    @Query(
            value = "select * from MLA.XXMLA_WQ_LINK_REGISTER wl where wl.MAAT_ID =?1 and wl.REMOVED_TX_ID is null",
            nativeQuery = true)
    List<WqLinkRegisterEntity> findBymaatId(Integer maatID);

    /**
     * @param maatId
     * @return
     */
    @Query(
            value = "SELECT count(*)  FROM MLA.XXMLA_WQ_LINK_REGISTER WHERE maat_id  = ?1 and removed_tx_id  is null ",
            nativeQuery = true)
    Integer getCountByMaatId(final Integer maatId);

    @Query(
            value =
                    """
                     SELECT * from MLA.XXMLA_WQ_LINK_REGISTER wl
                     WHERE wl.MAAT_ID =?1
                     AND REMOVED_TX_ID  IS NOT NULL
                     AND ROWNUM = 1
                     ORDER BY CREATED_TX_ID DESC
                 """,
            nativeQuery = true)
    Optional<WqLinkRegisterEntity> findUnlinkByMaat(Integer maatID);
}
