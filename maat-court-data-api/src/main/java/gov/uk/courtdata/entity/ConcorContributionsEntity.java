package gov.uk.courtdata.entity;

import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONCOR_CONTRIBUTIONS", schema = "TOGDATA")
@XmlType
public class ConcorContributionsEntity {

    @Id
    @SequenceGenerator(name = "concor_contributions_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concor_contributions_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "REP_ID")
    private Integer repId;

    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", length = 100)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 225)
    private String userModified;

    @Column(name = "SE_HISTORY_ID")
    private Integer seHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS",length = 20)
    private ConcorContributionStatus status;

    @Column(name = "CONTRIB_FILE_ID")
    private Integer contribFileId;

    @Column(name = "ACK_FILE_ID")
    private Integer ackFileId;

    @Column(name = "ACK_CODE", length = 20)
    private String ackCode;

    @Column(name = "FULL_XML")
    private String fullXml;

    @Column(name = "CURRENT_XML")
    private String currentXml;
}