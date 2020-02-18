package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_SESSION", schema = "MLA")
public class SessionEntity {
    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "COURT_LOCATION")
    private String courtLocation;
    @Column(name = "DATE_OF_HEARING")
    private LocalDate dateOfHearing;
    @Column(name = "POST_HEARING_CUSTODY")
    private String postHearingCustody;
    @Column(name = "SESSIONVALIDATEDATE")
    private LocalDate sessionvalidatedate;

}
