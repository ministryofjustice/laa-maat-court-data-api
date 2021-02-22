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
@Table(name = "XXMLA_WQ_PLEA", schema = "MLA")
public class WQPleaEntity {

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "PLEA_ID") private int pleaId;

    @Column(name = "PLEA_VALUE")
    private String pleaValue;

    @Column(name = "PLEA_DATE")
    private LocalDate pleaDate;

    @Column(name = "OFFENCE_ID")
    private String offenceId;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
}
