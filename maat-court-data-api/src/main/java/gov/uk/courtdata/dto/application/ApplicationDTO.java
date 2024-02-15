package gov.uk.courtdata.dto.application;

import gov.uk.courtdata.enums.EformEnum;
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
    private AreaTransferDetailsDTO areaTransferDTO;
    private ApplicantDTO applicantDTO;
    private AssessmentDTO assessmentDTO;
    private CapitalEquityDTO capitalEquityDTO;
    private CaseDetailDTO caseDetailsDTO;
    private CaseManagementUnitDTO caseManagementUnitDTO;
    private CrownCourtOverviewDTO crownCourtOverviewDTO;
    private LscTransferDTO lscTransferDTO;
    private MagsCourtDTO magsCourtDTO;
    private OutcomeDTO magsOutcomeDTO;

    private OffenceDTO offenceDTO;
    private PassportedDTO passportedDTO;
    private RepOrderDecisionDTO repOrderDecision;
    private RepStatusDTO statusDTO;
    private SupplierDTO supplierDTO;
    private ContraryInterestDTO partnerContraryInterestDTO;
    private AllowedWorkReasonDTO allowedWorkReasonDTO;
    private DrcSummaryDTO drcSummaryDTO;

    private String transactionId;

    private Boolean applicantHasPartner;
    private boolean welshCorrepondence;
    private String iojResult;
    private String iojResultNote;

    private String solicitorName;
    private String solicitorEmail;
    private String solicitorAdminEmail;

    private Collection<AssessmentSummaryDTO> assessmentSummary;
    private Collection<ApplicantLinkDTO> applicantLinks;

    private Collection<FdcContributionDTO> fdcContributions;


    private boolean courtCustody;

    private boolean retrial;

    private boolean messageDisplayed;

    private String alertMessage;
    private Long usn;

    private EformEnum applicationType;

    private ArrayList<DigitisedMeansAssessmentDTO> meansAssessments; // MW - 30/03/2017 - To support FIP changes

    private CommonPlatformDataDTO commonPlatformData;

    private BreathingSpaceDTO breathingSpaceDTO;

}
