package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.RepOrderBillingEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepOrderBillingRepository extends JpaRepository<RepOrderBillingEntity, Integer> {
    @Query(value = """
        SELECT  r.id
              , r.appl_id
              , r.arrest_summons_no
              , r.efel_fee_level
              , r.supp_account_code
              , r.maco_court
              , r.mcoo_outcome
              , r.date_received
              , r.cc_reporder_date
              , r.ofty_offence_type
              , r.cc_withdrawal_date
              , r.aphi_id
              , r.case_id
              , r.committal_date
              , r.rors_status
              , r.apty_code
              , r.ccoo_outcome
              , r.date_created
              , r.user_created
              , r.date_modified
              , r.user_modified
              , r.caty_case_type
        FROM    TOGDATA.REP_ORDERS r
        JOIN    TOGDATA.MAAT_REFS_TO_EXTRACT ex
        ON      r.ID = ex.MAAT_ID
    """, nativeQuery = true)
    List<RepOrderBillingEntity> getRepOrdersForBilling();

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE  TOGDATA.REP_ORDERS r
        SET     r.SEND_TO_CCLF = null,
                r.DATE_MODIFIED = CURRENT_DATE,
                r.USER_MODIFIED = :userModified
        WHERE   r.ID IN :ids
    """, nativeQuery = true)
    int resetBillingFlagForRepOrderIds(@Param("userModified") String userModified, @Param("ids") List<Integer> ids);
}
