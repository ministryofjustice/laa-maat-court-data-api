package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.ContributionFileErrorsId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTRIBUTION_FILE_ERRORS", schema = "TOGDATA")
@IdClass(ContributionFileErrorsId.class)
@XmlType
public class ContributionFileErrorsEntity {

    @Id
    @Column(name = "CONTR_FILE_ID")
    private Integer contributionFileId;

    @Id
    @Column(name = "CONTR_ID", length = 100, nullable = false)
    private Integer contributionId;

    @Column(name = "REP_ID")
    private Integer repId;

    @Column(name = "ERROR_TEXT")
    private String errorText;

    @Column(name = "FIX_ACTION")
    private String fixAction;

    @Column(name = "FDC_CONTRIB_ID")
    private Integer fdcContributionId;

    @Column(name = "CONCOR_CONTRIB_ID")
    private Integer concorContributionId;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private LocalDate dateCreated;
}
