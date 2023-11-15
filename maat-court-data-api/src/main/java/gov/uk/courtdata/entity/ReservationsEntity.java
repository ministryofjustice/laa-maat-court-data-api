package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESERVATIONS", schema = "TOGDATA")
public class ReservationsEntity {

    @Id
    @Column(name = "RECORD_ID")
    private Integer recordId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_SESSION")
    private String userSession;

    @Column(name = "RECORD_NAME")
    private String recordName;

    @Column(name = "RESERVATION_DATE")
    private LocalDateTime reservationDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDateTime expiryDate;
}
