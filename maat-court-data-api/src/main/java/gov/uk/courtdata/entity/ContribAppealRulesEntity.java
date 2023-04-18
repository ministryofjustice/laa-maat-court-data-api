package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CONTRIB_APPEAL_RULES", schema = "TOGDATA")
public class ContribAppealRulesEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CATY_CASE_TYPE", nullable = false, length = 20)
    private String catyCaseType;

    @Column(name = "APTY_CODE", nullable = false, length = 3)
    private String aptyCode;

    @Column(name = "CCOO_OUTCOME", nullable = false, length = 3)
    private String ccooOutcome;

    @Column(name = "CONTRIB_AMOUNT")
    private Integer contribAmount;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "ASSESSMENT_RESULT", length = 4)
    private String assessmentResult;

}