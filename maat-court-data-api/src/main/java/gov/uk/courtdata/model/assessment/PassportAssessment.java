package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PassportAssessment {
    private Integer repId;
    private String nworCode;
    private Integer cmuId;
    private LocalDateTime assessmentDate;
    private String partnerBenefitClaimed;
    private String partnerFirstName;
    private String partnerSurname;
    private String partnerOtherNames;
    private String partnerNiNumber;
    private LocalDateTime partnerDob;
    private String incomeSupport;
    private String jobSeekers;
    private String statePensionCredit;
    private String under18FullEducation;
    private String under16;
    private String pcobConfirmation;
    private String result;
    private String dwpResult;
    private String passportNote;
    private String between16And17;
    private String under18HeardInYouthCourt;
    private String under18HeardInMagsCourt;
    private LocalDateTime lastSignOnDate;
    private String esa;
    private String pastStatus;
    @Builder.Default
    private String replaced = "N";
    private LocalDateTime passportEvidenceDueDate;
    private LocalDateTime allPassportEvidenceReceivedDate;
    private Integer passportUpliftPercentage;
    private LocalDateTime passportUpliftApplyDate;
    private LocalDateTime passportUpliftRemoveDate;
    private String passportEvidenceNotes;
    private LocalDateTime firstPassportReminderDate;
    private LocalDateTime secondPassportReminderDate;
    private String valid;
    private LocalDateTime dateCompleted;
    private String whoDWPChecked;

}
