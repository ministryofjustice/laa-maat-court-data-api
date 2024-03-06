package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PassportedDTO extends GenericDTO {
    private Long usn;
    private Long passportedId;
    private Long cmuId;
    private Date date;
    private String dwpResult;
    private Boolean benefitIncomeSupport;
    private Boolean benefitGaurenteedStatePension;
    private Boolean benefitClaimedByPartner;
    private Boolean benefitEmploymentSupport;
    private Boolean benefitUniversalCredit;
    private String notes;
    private String whoDwpChecked;
    private String result;
    private Boolean under18HeardYouthCourt;
    private Boolean under18HeardMagsCourt;
    private Boolean under18FullEducation;
    private Boolean under16;
    private Boolean between1617;

    @Builder.Default
    private PartnerDTO partnerDetails = new PartnerDTO();
    @Builder.Default
    private JobSeekerDTO benefitJobSeeker = new JobSeekerDTO();
    @Builder.Default
    private AssessmentStatusDTO assessementStatusDTO = new AssessmentStatusDTO();
    @Builder.Default
    private PassportConfirmationDTO passportConfirmationDTO = new PassportConfirmationDTO();
    @Builder.Default
    private NewWorkReasonDTO newWorkReason = new NewWorkReasonDTO();
    @Builder.Default
    private ReviewTypeDTO reviewType = new ReviewTypeDTO();
    @Builder.Default
    private IncomeEvidenceSummaryDTO passportSummaryEvidenceDTO = new IncomeEvidenceSummaryDTO();
}