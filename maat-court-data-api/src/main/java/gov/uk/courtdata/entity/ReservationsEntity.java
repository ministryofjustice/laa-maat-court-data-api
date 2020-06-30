package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESERVATIONS", schema = "TOGDATA")
public class ReservationsEntity {

    @Id
    @Column(name = "RECORD_ID")
    private String recordId;

    @Column(name = "USER_NAME")
    private String userName;
}
