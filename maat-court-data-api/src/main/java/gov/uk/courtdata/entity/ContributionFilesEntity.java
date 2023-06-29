package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CONTRIBUTION_FILES", schema = "TOGDATA")
public class ContributionFilesEntity {
    @Id
    @SequenceGenerator(name = "contributions_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contributions_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FILE_NAME", nullable = false, length = 45)
    private String fileName;

    @Column(name = "RECORDS_SENT", nullable = false)
    private Integer recordsSent;

    @Column(name = "RECORDS_RECEIVED")
    private Integer recordsReceived;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 250)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @ColumnTransformer(read = "to_clob(XML_CONTENT)", write = "?")
    @Column(name = "XML_CONTENT", columnDefinition = "XMLType")
    private String xmlContent;

    @Column(name = "USER_MODIFIED", length = 250)
    private String userModified;

    @Column(name = "DATE_SENT")
    private LocalDateTime dateSent;

    @Column(name = "DATE_RECEIVED")
    private LocalDateTime dateReceived;

    @ColumnTransformer(read = "to_clob(ACK_XML_CONTENT)", write = "?")
    @Column(name = "ACK_XML_CONTENT", columnDefinition = "XMLType")
    private String ackXmlContent;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "contributionFile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContributionsEntity> contributionsEntities;
}
