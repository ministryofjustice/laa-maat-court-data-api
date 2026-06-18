package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DLRM_STATUS_UPDATE", schema = "MLA")
public class DlrmStatusUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dlrm_status_update_seq")
    @SequenceGenerator(name = "dlrm_status_update_seq", sequenceName = "DLRM_STATUS_UPDATE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MAAT_ID")
    private Integer maatId;

    @Column(name = "OFFENCE_ID")
    private String offenceId;

    @Column(name = "OFFENCE_CODE")
    private String offenceCode;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "DATE_CREATED")
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
