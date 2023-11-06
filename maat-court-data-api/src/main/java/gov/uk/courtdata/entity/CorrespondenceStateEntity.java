package gov.uk.courtdata.entity;

import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CORRESPONDENCE_STATE", schema = "TOGDATA")
public class CorrespondenceStateEntity {

    @Id
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;

    @Column(name = "STATUS", length = 20)
    private String status;

}
