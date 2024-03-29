package gov.uk.courtdata.eform.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
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
    private String xmlDoc;

    @Column(name = "MAAT_REF")
    private Integer maatRef;

    @Column(name = "MAAT_STATUS")
    private String maatStatus;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
}
