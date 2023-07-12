package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTRIBUTION_FILES", schema = "TOGDATA")
@XmlType
public class ContributionFilesEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FILE_NAME", length = 100, nullable = false)
    private String fileName;

    @Column(name = "RECORDS_SENT")
    private Integer recordsSent;

    @Column(name = "RECORDS_RECEIVED")
    private Integer recordsReceived;

    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 225)
    private String userModified;

    @Column(name = "XML_CONTENT")
    private String xmlContent;

    @Column(name = "DATE_SENT")
    private LocalDate dateSent;

    @Column(name = "DATE_RECEIVED")
    private LocalDate dateReceived;

    @Column(name = "ACK_XML_CONTENT")
    private String ackXmlContent;
}