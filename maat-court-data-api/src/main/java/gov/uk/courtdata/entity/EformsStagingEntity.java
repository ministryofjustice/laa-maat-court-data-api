package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Clob;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EFORMS_STAGING", schema = "HUB")
public class EformsStagingEntity {

    @Id
    @Column(name = "USN")
    private Integer usn;

    @Column(name = "TYPE")
    private String type;

    @Lob
    @Column(name = "XML_DOC")
    private Clob xmlDoc;

    @Column(name = "MAAT_REF")
    private String maatRef;

    @Column(name = "MAAT_STATUS")
    private String maatStatus;

    @Column(name = "USER_CREATED", updatable = false)
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
}
