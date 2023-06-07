package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTRIBUTION_FILES", schema = "TOGDATA")
public class ContributionFilesEntity {

    @Id
    @SequenceGenerator(name = "contribution_files_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contribution_files_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FILE_NAME", length = 45,  nullable = false)
    private String upliftApplied;

    @Column(name = "DATE_CREATED",  nullable = false)
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", length = 225, nullable = false)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 225)
    private String userModified;

    @Column(name = "DATE_SENT")
    private LocalDate dateSent;

    @Column(name = "DATE_RECEIVED")
    private LocalDate dateReceived;

    @Column(name = "XML_CONTENT")
    private String xmlContent;


}