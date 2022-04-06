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
@Table(name = "CONTRARY_INTEREST_REASONS")
public class ContraryInterestReason {
    @Id
    @Column(name = "CODE", nullable = false, length = 10)
    private String id;

    //TODO Reverse Engineering! Migrate other columns to the entity
}