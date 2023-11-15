package gov.uk.courtdata.entity;
import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.xml.bind.annotation.XmlType;
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
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "REP_ID")
    private Integer repId;

    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", length = 100)
    private String userCreated;

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