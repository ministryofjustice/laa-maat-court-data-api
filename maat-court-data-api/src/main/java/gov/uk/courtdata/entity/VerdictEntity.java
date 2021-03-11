package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_VERDICT", schema = "MLA")
public class VerdictEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;

    @Column(name = "OFFENCE_ID")
    private String offenceId;

    @Column(name = "VERDICT_DATE")
    private LocalDate verdictDate;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CATEGORY_TYPE")
    private String categoryType;

    @Column(name = "CJS_VERDICT_CODE")
    private String cjsVerdictCode;

    @Column(name = "VERDICT_CODE")
    private String verdictCode;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "CASE_ID")
    private Integer caseId;

    @Column(name = "MAAT_ID")
    private Integer maatId;
}