package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_WQ_VERDICT", schema = "MLA")
public class WQVerdictEntity {

    @Id
    @Column(name = "VERDICT_ID")
    private Integer verdictId;

    @Column(name = "OFFENCE_ID")
    private String offenceId;

    @Column(name = "VERDICT_DATE")
    private Date verdictDate;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CATEGORY_TYPE")
    private String categoryType;

    @Column(name = "CJS_VERDICT_CODE")
    private String cjsVerdictCode;

    @Column(name = "VERDICT_CODE")
    private String verdictCode;
}
