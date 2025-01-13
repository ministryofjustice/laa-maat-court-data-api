package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ReservationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReservationsRepository extends JpaRepository<ReservationsEntity, Integer> {

    @Query(value = "SELECT * FROM TOGDATA.RESERVATIONS WHERE USER_SESSION = :userSession AND USER_NAME = :username AND RECORD_NAME = :recordName AND RECORD_ID = :recordId AND EXPIRY_DATE > SYSDATE", nativeQuery = true)
    Optional<ReservationsEntity> findReservationByUserSession(String userSession, String username, String recordName, Integer recordId);

    @Query(value = "SELECT * FROM TOGDATA.RESERVATIONS WHERE RECORD_NAME = :recordName AND RECORD_ID = :recordId AND EXPIRY_DATE > SYSDATE", nativeQuery = true)
    Optional<ReservationsEntity> findReservationByRecordNameAndRecordId(String recordName, Integer recordId);

    @Query(value = "SELECT * FROM TOGDATA.RESERVATIONS WHERE USER_NAME = :username AND EXPIRY_DATE > SYSDATE", nativeQuery = true)
    Optional<ReservationsEntity> findReservationByUserName(String username);


    @Modifying
    @Query(value =
"""
    UPDATE TOGDATA.RESERVATIONS r SET EXPIRY_DATE = SYSDATE +
    (SELECT cp."VALUE" FROM TOGDATA.CONFIG_PARAMETERS cp WHERE cp.CODE = 'RESERVATION_TIME' AND cp.EFFECTIVE_DATE =
    (SELECT MAX(cp2.EFFECTIVE_DATE) FROM TOGDATA.CONFIG_PARAMETERS cp2 WHERE cp2.CODE = 'RESERVATION_TIME' AND cp2.EFFECTIVE_DATE < SYSDATE)) / 24
    WHERE r.USER_SESSION = :userSession AND r.USER_NAME = :username AND r.RECORD_NAME = :recordName AND r.RECORD_ID = :recordId
""", nativeQuery = true)
    void updateReservationExpiryDate(String userSession, String username, String recordName, Integer recordId);

//    @Modifying
//    @Query(value = "UPDATE TOGDATA.CONFIG_PARAMETERS set CODE='RESERVATION_TIME' WHERE CODE='RESERVATION_TIME'", nativeQuery = true)
//    void updateReservationExpiryDate(String userSession, String username, String recordName, Integer recordId);

      // WORKING!!!
//    @Modifying
//    @Query(value =
//"""
//    UPDATE TOGDATA.RESERVATIONS r SET EXPIRY_DATE = SYSDATE +
//    (SELECT CAST("VALUE" AS NUMERIC) FROM TOGDATA.CONFIG_PARAMETERS cp WHERE cp.CODE='RESERVATION_TIME')
//    WHERE r.USER_SESSION = :userSession AND r.USER_NAME = :username AND r.RECORD_NAME = :recordName AND r.RECORD_ID = :recordId
//""", nativeQuery = true)
//    void updateReservationExpiryDate(String userSession, String username, String recordName, Integer recordId);

//    @Modifying
//    @Query(value = "UPDATE TOGDATA.RESERVATIONS r JOIN TOGDATA.CONFIG_PARAMETERS cp ON cp.CODE = 'RESERVATION_TIME' SET r.EXPIRY_DATE = SYSDATE + (cp.VALUE / 24) WHERE r.USER_SESSION = :userSession AND r.USER_NAME = :username AND r.RECORD_NAME = :recordName AND r.RECORD_ID = :recordId", nativeQuery = true)
//    void updateReservationExpiryDate(String userSession, String username, String recordName, Integer recordId);

//    @Modifying
//    @Query(value =
//        """
//            UPDATE TOGDATA.RESERVATIONS r
//            JOIN TOGDATA.CONFIG_PARAMETERS cp ON cp.CODE = 'RESERVATION_TIME' AND cp.EFFECTIVE_DATE = '2022-01-01'
//            SET r.EXPIRY_DATE = SYSDATE + (cp.VALUE / 24)
//            WHERE r.USER_SESSION = :userSession AND r.USER_NAME = :username AND r.RECORD_NAME = :recordName AND r.RECORD_ID = :recordId
//        """, nativeQuery = true)
//    void updateReservationExpiryDate(String userSession, String username, String recordName, Integer recordId);
}
