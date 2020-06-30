package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Value
@Entity
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@NoArgsConstructor (force = true)
@Table(name = "RESERVATIONS", schema = "TOGDATA")
public class ReservationsEntity {

    @Id
    @Column(name = "RECORD_ID")
    private String recordId;

    @Column(name = "USER_SESSION")
    private String userSession;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "RECORD_NAME")
    private String recordName;

    @Column(name = "RESERVATION_DATE")
    private LocalDate reservationDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;
}
