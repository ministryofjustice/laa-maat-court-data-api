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
@Table(name = "XXMLA_PLEA", schema = "MLA")
public class PleaEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;

    @Column(name = "PLEA_VALUE")
    private String pleaValue;

    @Column(name = "PLEA_DATE")
    private LocalDate pleaDate;

    @Column(name = "OFFENCE_ID")
    private String offenceId;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "CASE_ID")
    private Integer caseId;

    @Column(name = "MAAT_ID")
    private Integer maatId;
}
