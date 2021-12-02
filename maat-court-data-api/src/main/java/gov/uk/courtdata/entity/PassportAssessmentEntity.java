package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PASSPORT_ASSESSMENTS", schema = "TOGDATA")
public class PassportAssessmentEntity {
    @Id
    @SequenceGenerator(name = "passport_ass_seq", sequenceName = "S_PASSPORT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passport_ass_seq")
    private Integer id;
    @Column(name = "REP_ID")
    private Integer repId;
    @Column(name = "NWOR_CODE")
    private String nworCode;
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "CMU_ID")
    private Integer cmuId;
    @Column(name = "ASS_DATE")
    private LocalDateTime assessmentDate;
    @Column(name = "PARTNER_BENEFIT_CLAIMED")
    private String partnerBenefitClaimed;
    @Column(name = "PARTNER_FIRST_NAME")
    private String partnerFirstName;
    @Column(name = "PARTNER_SURNAME")
    private String partnerSurname;
    @Column(name = "PARTNER_OTHER_NAMES")
    private String partnerOtherNames;
    @Column(name = "PARTNER_NI_NO")
    private String partnerNiNumber;
    @Column(name = "PARTNER_DOB")
    private LocalDateTime partnerDob;
    @Column(name = "INCOME_SUPPORT")
    private String incomeSupport;
    @Column(name = "JOB_SEEKERS")
    private String jobSeekers;
    @Column(name = "STATE_PENSION_CREDIT")
    private String statePensionCredit;
    @Column(name = "UNDER_18_FULL_EDUCATION")
    private String under18FullEducation;
    @Column(name = "UNDER_16")
    private String under16;
    @Column(name = "PCOB_CONFIRMATION")
    private String pcobConfirmation;
    @Column(name = "RESULT")
    private String result;
    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "DWP_RESULT")
    private String dwpResult;
    @Column(name = "PASSPORT_NOTE")
    private String passportNote;
    @Column(name = "BETWEEN_16_17")
    private String between16And17;
    @Column(name = "UNDER_18_HEARD_YOUTH_COURT")
    private String under18HeardInYouthCourt;
    @Column(name = "UNDER_18_HEARD_MAGS_COURT")
    private String under18HeardInMagsCourt;
    @Column(name = "LAST_SIGN_ON_DATE")
    private LocalDateTime lastSignOnDate;
    @Column(name = "ESA")
    private String esa;
    @Column(name = "PAST_STATUS")
    private String pastStatus;
    @Builder.Default
    @Column(name = "REPLACED")
    private String replaced = "N";
    @Column(name = "PASSPORT_EVIDENCE_DUE_DATE")
    private LocalDateTime passportEvidenceDueDate;
    @Column(name = "ALL_PP_EVIDENCE_REC_DATE")
    private LocalDateTime allPassportEvidenceReceivedDate;
    @Column(name = "PASSPORT_UPLIFT_PERCENTAGE")
    private Integer passportUpliftPercentage;
    @Column(name = "PASSPORT_UPLIFT_APPLY_DATE")
    private LocalDateTime passportUpliftApplyDate;
    @Column(name = "PASSPORT_UPLIFT_REMOVE_DATE")
    private LocalDateTime passportUpliftRemoveDate;
    @Column(name = "PASSPORT_EVIDENCE_NOTES")
    private String passportEvidenceNotes;
    @Column(name = "FIRST_PP_REMINDER_DATE")
    private LocalDateTime firstPassportReminderDate;
    @Column(name = "SECOND_PP_REMINDER_DATE")
    private LocalDateTime secondPassportReminderDate;
    @Column(name = "VALID")
    private String valid;
    @Column(name = "DATE_COMPLETED")
    private LocalDateTime dateCompleted;
    @Column(name = "USN")
    private Integer usn;
    @Column(name = "WHO_DWP_CHECKED")
    private String whoDWPChecked;
    @Column(name = "RT_CODE")
    private String rtCode;
}
