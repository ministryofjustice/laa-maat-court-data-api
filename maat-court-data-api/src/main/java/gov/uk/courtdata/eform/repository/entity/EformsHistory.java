package gov.uk.courtdata.eform.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EFORMS_HISTORY", schema = "TOGDATA")
public class EformsHistory {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "eforms_history_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eforms_history_seq")
    private Integer id;
    @Column(name = "USN", nullable = false)
    private Integer usn;
    @Column(name = "REP_ID")
    private Integer repId;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "KEY_ID")
    private Integer keyId;
    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;
}
