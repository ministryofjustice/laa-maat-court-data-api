package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.CaseTxnId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CaseTxnId.class)
@Table(name = "XXMLA_SESSION", schema = "MLA")
public class SessionEntity {
    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Id
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "COURT_LOCATION")
    private String courtLocation;
    @Column(name = "DATE_OF_HEARING")
    private LocalDate dateOfHearing;
    @Column(name = "POST_HEARING_CUSTODY")
    private String postHearingCustody;
    @Column(name = "SESSIONVALIDATEDATE")
    private LocalDate sessionValidateDate;

}
