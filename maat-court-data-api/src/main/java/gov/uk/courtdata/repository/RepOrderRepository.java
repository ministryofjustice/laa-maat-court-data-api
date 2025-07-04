package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Repository
public interface RepOrderRepository extends JpaRepository<RepOrderEntity, Integer>, JpaSpecificationExecutor<RepOrderEntity> {
    RepOrderEntity findByUsn(Integer usn);

    @Query(value = """
                        SELECT *
                        FROM (
                            SELECT DISTINCT RO.ID
                            FROM
                                TOGDATA.CONCOR_CONTRIBUTIONS CC,
                                TOGDATA.REP_ORDERS RO,
                                TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES ROCCO
                            WHERE
                                CC.REP_ID = RO.ID
                                AND RO.ID = ROCCO.REP_ID
                                AND CC.STATUS = 'SENT'
                                AND ROCCO.CCOO_OUTCOME IS NOT NULL
                                AND RO.SENTENCE_ORDER_DATE IS NOT NULL
                                AND TRUNC( ADD_MONTHS( NVL(RO.SENTENCE_ORDER_DATE, SYSDATE ), :delayPeriod) ) <= TRUNC(SYSDATE)
                                AND RO.DATE_RECEIVED< :dateReceived
                            )
                        WHERE ROWNUM <= :numRecords
                        """, nativeQuery = true)
    Set<Integer> findEligibleForFdcDelayedPickup(@Param("delayPeriod") int delayPeriod, @Param("dateReceived") LocalDate dateReceived, @Param("numRecords") int numRecords);

    @Query(value = """
                        SELECT *
                        FROM (
                            SELECT DISTINCT RO.ID
                            FROM
                                TOGDATA.CONCOR_CONTRIBUTIONS CC,
                                TOGDATA.REP_ORDERS RO,
                                TOGDATA.REP_ORDER_CROWN_COURT_OUTCOMES ROCCO
                            WHERE
                                CC.REP_ID = RO.ID
                                AND RO.ID = ROCCO.REP_ID
                                AND CC.STATUS = 'SENT'
                                AND ROCCO.CCOO_OUTCOME IS NOT NULL
                                AND RO.SENTENCE_ORDER_DATE IS NOT NULL AND RO.DATE_RECEIVED > :dateReceived
                                AND RO.SENTENCE_ORDER_DATE < ADD_MONTHS(TRUNC(SYSDATE),:delayPeriod)
                            )
                        WHERE ROWNUM <= :numRecords
                        """, nativeQuery = true)
    Set<Integer> findEligibleForFdcFastTracking(@Param("delayPeriod") int delayPeriod, @Param("dateReceived") LocalDate dateReceived, @Param("numRecords") int numRecords);

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
                        FROM    REP_ORDERS r
                        JOIN    MAAT_REFS_TO_EXTRACT ex
                        ON      r.ID = ex.MAAT_ID
    """, nativeQuery = true)
    List<RepOrderEntity> getRepOrdersForBilling();

}

