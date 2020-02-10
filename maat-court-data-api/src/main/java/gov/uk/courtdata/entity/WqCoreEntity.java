package gov.uk.courtdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_WQ_CORE", schema = "MLA")
public class WqCoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "WQ_TYPE")
    private Integer wqType;
    @Column(name = "WQ_STATUS")
    private Integer wqStatus;
    @Column(name = "CREATED_USER_ID")
    private String createdUserId;
    @Column(name = "LOCK_TIME")
    private LocalDate lockTime;
    @Column(name = "LOCK_USER_ID")
    private String lockUserId;
    @Column(name = "CREATED_TIME")
    private LocalDate createdTime;
    @Column(name = "PROCESSED_USER_ID")
    private String processedUserId;
    @Column(name = "PROCESSED_TIME")
    private LocalDate processedTime;
    @Column(name = "XML_ID")
    private Integer xmlID;
    @Column(name = "EXTENDED_PROCESSING")
    private Integer extendedProcessing;
    @Column(name = "RETRY_FLAG")
    private Integer retryFlag;
    @Column(name = "MAAT_UPDATE_FLAG")
    private Integer maatUpdateFlag;
}
