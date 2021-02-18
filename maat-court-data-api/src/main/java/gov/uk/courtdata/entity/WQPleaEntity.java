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
@Table(name = "XXMLA_WQ_PLEA", schema = "MLA")
public class WQPleaEntity extends BaseEntity {

    //todo: auto generated?
    @Id
    @Column(name = "PLEA_ID")
    private Integer pleaId;

    @Column(name = "PLEA_VALUE")
    private String pleaValue;

    @Column(name = "PLEA_DATE")
    private Date pleaDate;

    @Column(name = "OFFENCE_ID")
    private String offenceId;
}
