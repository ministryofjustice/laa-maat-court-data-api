package gov.uk.courtdata.billing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "APPLICANT_HISTORY", schema = "TOGDATA")
public class ApplicantHistoryBillingEntity {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "applicant_history_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applicant_history_gen_seq")
    private Integer id;

    @Column(name = "APPL_ID")
    private Integer applId;

    @Column(name = "AS_AT_DATE")
    private LocalDate asAtDate;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "OTHER_NAMES")
    private String otherNames;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "NI_NUMBER")
    private String niNumber;

    @Column(name = "FOREIGN_ID")
    private String foreignId;

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
