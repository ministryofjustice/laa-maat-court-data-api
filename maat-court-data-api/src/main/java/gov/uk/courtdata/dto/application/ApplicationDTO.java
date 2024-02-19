package gov.uk.courtdata.dto.application;

import gov.uk.courtdata.enums.EformEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationDTO extends GenericDTO {
    private Long repId;
    private Long areaId;

    private String caseId;
    private String arrestSummonsNo;
    private String statusReason;

    private Date dateCreated;
    private Date dateReceived;
    private Date dateOfSignature;
    private Date committalDate;
    private SysGenDate magsCourtOutcomeDate;
    private Date magsWithdrawalDate;
    private SysGenDate dateStatusSet;
    private Date dateStatusDue;
    private Date decisionDate;
    private Date dateStamp;
    private Date hearingDate;
    private String transactionId;
    private Boolean applicantHasPartner;
    private boolean welshCorrepondence;
    private String iojResult;
    private String iojResultNote;
    private String solicitorName;
    private String solicitorEmail;
    private String solicitorAdminEmail;
    private boolean courtCustody;
    private boolean retrial;
    private boolean messageDisplayed;
    private String alertMessage;
    private Long usn;
    private EformEnum applicationType;

    @Builder.Default
    private AreaTransferDetailsDTO areaTransferDTO = new AreaTransferDetailsDTO();
    @Builder.Default
    private ApplicantDTO applicantDTO = new ApplicantDTO();
    @Builder.Default
    private AssessmentDTO assessmentDTO = new AssessmentDTO();
    @Builder.Default
    private CapitalEquityDTO capitalEquityDTO = new CapitalEquityDTO();
    @Builder.Default
    private CaseDetailDTO caseDetailsDTO = new CaseDetailDTO();
    @Builder.Default
    private CaseManagementUnitDTO caseManagementUnitDTO = new CaseManagementUnitDTO();
    @Builder.Default
    private CrownCourtOverviewDTO crownCourtOverviewDTO = new CrownCourtOverviewDTO();
    @Builder.Default
    private LscTransferDTO lscTransferDTO = new LscTransferDTO();
    @Builder.Default
    private MagsCourtDTO magsCourtDTO = new MagsCourtDTO();
    @Builder.Default
    private OutcomeDTO magsOutcomeDTO = new OutcomeDTO();
    @Builder.Default
    private OffenceDTO offenceDTO = new OffenceDTO();
    @Builder.Default
    private PassportedDTO passportedDTO = new PassportedDTO();
    @Builder.Default
    private RepOrderDecisionDTO repOrderDecision = new RepOrderDecisionDTO();
    @Builder.Default
    private RepStatusDTO statusDTO = new RepStatusDTO();
    @Builder.Default
    private SupplierDTO supplierDTO = new SupplierDTO();
    @Builder.Default
    private ContraryInterestDTO partnerContraryInterestDTO = new ContraryInterestDTO();
    @Builder.Default
    private AllowedWorkReasonDTO allowedWorkReasonDTO = new AllowedWorkReasonDTO();
    @Builder.Default
    private DrcSummaryDTO drcSummaryDTO = new DrcSummaryDTO();
    @Builder.Default
    private Collection<AssessmentSummaryDTO> assessmentSummary = new ArrayList<>();
    @Builder.Default
    private Collection<ApplicantLinkDTO> applicantLinks = new ArrayList<>();
    @Builder.Default
    private Collection<FdcContributionDTO> fdcContributions = new ArrayList<>();
    @Builder.Default
    private ArrayList<DigitisedMeansAssessmentDTO> meansAssessments = new ArrayList<>();
    @Builder.Default
    private CommonPlatformDataDTO commonPlatformData = new CommonPlatformDataDTO();
    @Builder.Default
    private BreathingSpaceDTO breathingSpaceDTO = new BreathingSpaceDTO();
}
