package gov.uk.courtdata.entity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
        @SequenceGenerator(name = "contributions_files_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contributions_files_gen_seq")
        @Column(name = "ID", nullable = false)
        private Integer id;

        @Column(name = "FILE_NAME", length = 100, nullable = false)
        private String fileName;

        @Column(name = "RECORDS_SENT")
        private Integer recordsSent;

        @Column(name = "RECORDS_RECEIVED")
        private Integer recordsReceived;

        @CreationTimestamp
        @Column(name = "DATE_CREATED")
        private LocalDate dateCreated;

        @Builder.Default
        @Column(name = "USER_CREATED")
        private String userCreated = "DCES";

        @Column(name = "DATE_MODIFIED")
        private LocalDate dateModified;

        @Column(name = "USER_MODIFIED", length = 225)
        private String userModified;

        @Column(name = "XML_CONTENT")
        private String xmlContent;

        @UpdateTimestamp
        @Column(name = "DATE_SENT")
        private LocalDate dateSent;

        @Column(name = "DATE_RECEIVED")
        private LocalDate dateReceived;

        @Column(name = "ACK_XML_CONTENT")
        private String ackXmlContent;
    }