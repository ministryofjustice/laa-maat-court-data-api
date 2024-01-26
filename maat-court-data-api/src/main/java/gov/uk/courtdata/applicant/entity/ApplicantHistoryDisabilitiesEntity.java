package gov.uk.courtdata.applicant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "APPLICANT_HISTORY_DISABILITIES", schema = "TOGDATA")
public class ApplicantHistoryDisabilitiesEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "applicant_history_disabilities_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_history_disabilities_gen_seq")
    private Integer id;

    @Column(name = "DISA_DISABILITY")
    private String disaDisability;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;
}
