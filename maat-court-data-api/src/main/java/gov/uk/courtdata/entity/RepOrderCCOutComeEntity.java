package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "REP_ORDER_CROWN_COURT_OUTCOMES", schema = "TOGDATA")
public class RepOrderCCOutComeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    @Column(name = "REP_ID")
    private int repId;
    @Column(name = "CCOO_OUTCOME")
    private String ccooOutcome;
    @Column(name = "CCOO_OUTCOME_DATE")
    private LocalDateTime ccooOutcomeDate;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "DATE_CREATED")
    @CreationTimestamp
    private LocalDateTime dateCreated;
    @Column(name = "CASE_NUMBER")
    private String caseNumber;
    @Column(name = "CROWN_COURT_CODE")
    private String crownCourtCode;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "DATE_MODIFIED")
    @UpdateTimestamp
    private LocalDateTime dateModified;
}
