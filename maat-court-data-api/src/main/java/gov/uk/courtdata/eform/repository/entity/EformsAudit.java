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
@Table(name = "EFORMS_AUDIT", schema = "TOGDATA")
public class EformsAudit {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USN")
    private Integer usn;

    @Column(name = "MAAT_REF")
    private Integer maatRef;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "STATUS_CODE")
    private String statusCode;
}
