package gov.uk.courtdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_SESSION", schema = "MLA")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "COURT_LOCATION")
    private String courtLocation;
    @Column(name = "DATE_OF_HEARING")
    private LocalDate dateOfHearing;
    @Column(name = "POST_HEARING_CUSTODY")
    private String postHearingCustody;
    @Column(name = "SESSIONVALIDATEDDATE")
    private LocalDate sessionvalidateddate;

}
