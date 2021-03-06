package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
