package gov.uk.courtdata.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_PROSECUTION_CONCLUDED", schema = "MLA")
public class ProsecutionConcludedEntity {


    @Id
    @SequenceGenerator(name = "case_con_seq", sequenceName = "CASE_CONCLUSION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_con_seq")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "HEARING_ID")
    private String hearingId;
    @Lob
    @Column(name = "CASE_DATA")
    private byte[] caseData;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;
    @Column(name = "UPDATED_TIME")
    private LocalDateTime updatedTime;
    @Column(name = "RETRY_COUNT")
    private Integer retryCount;


}
