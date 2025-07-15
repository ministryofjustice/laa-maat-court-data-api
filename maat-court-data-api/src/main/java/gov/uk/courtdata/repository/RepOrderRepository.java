package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

}

