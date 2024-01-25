package gov.uk.courtdata.applicant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "APPLICANT_DISABILITIES", schema = "TOGDATA")
public class ApplicantDisabilitiesEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "applicant_disabilities_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_disabilities_gen_seq")
    private Integer id;

    @Column(name = "APPL_ID")
    private Integer applId;

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

    @ToString.Exclude
    @OneToMany(mappedBy = "applicantDisabilities", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ApplicantHistoryDisabilitiesEntity> applicantHistoryDisabilityEntities = new ArrayList<>();
}
