package gov.uk.courtdata.applicant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "APPLICANT_HISTORY_DISABILITIES", schema = "TOGDATA")
public class ApplicantHistoryDisability {
    @Id
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "applicant_history_disabilities_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_history_disabilities_gen_seq")
    private Integer id;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "APHI_ID", nullable = false)
    @MapsId(value = "id")
    private ApplicantHistoryEntity aphi;

    @NotNull
    @JoinColumn(name = "DISA_DISABILITY", nullable = false)
    private String disaDisability;

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Size(max = 100)
    @NotNull
    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Size(max = 100)
    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

}