package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "CROWN_COURT_OUTCOMES", schema = "TOGDATA")
public class CrownCourtOutComeEntity {

    @Id
    @Column(name = "OUTCOME")
    private String outcome;
}
