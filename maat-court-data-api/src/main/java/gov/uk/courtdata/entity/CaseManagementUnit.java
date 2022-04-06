package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CASE_MANAGEMENT_UNITS")
public class CaseManagementUnit {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    //TODO Reverse Engineering! Migrate other columns to the entity
}