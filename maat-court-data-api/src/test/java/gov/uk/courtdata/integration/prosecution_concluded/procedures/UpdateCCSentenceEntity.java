package gov.uk.courtdata.integration.prosecution_concluded.procedures;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "UPDATE_OUTCOMES", schema = "TOGDATA")
public class UpdateCCSentenceEntity {
    @Id
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    @Column(name = "CC_OUTCOME", nullable = false)
    private String ccOutcome;
    @Column(name = "BENCH_WARRANT_ISSUED", nullable = false)
    private String benchWarrantIssued;
    @Column(name = "APPEAL_TYPE", nullable = false)
    private String appealType;
    @Column(name = "IMPRISONED", nullable = false)
    private String imprisoned;
    @Column(name = "CASE_NUMBER", nullable = false)
    private String caseNumber;
    @Column(name = "CROWN_COURT_CODE", nullable = false)
    private String crownCourtCode;

}
