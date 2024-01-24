package gov.uk.courtdata.applicant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "APPLICANT_HISTORY_DISABILITIES", schema = "TOGDATA")
public class ApplicantHistoryDisabilities {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "applicant_history_disabilities_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_history_disabilities_gen_seq")
    private Integer id;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "APHI_ID", nullable = false, updatable = false)
    private ApplicantHistoryDisabilities applicantHistoryDisabilities;

    @Column(name = "DISA_DISABILITY")
    private String disaDisability;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;
}
