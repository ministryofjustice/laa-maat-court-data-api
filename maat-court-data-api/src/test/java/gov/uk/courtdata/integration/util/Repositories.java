package gov.uk.courtdata.integration.util;

import gov.uk.courtdata.applicant.repository.ApplicantDisabilitiesRepository;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.billing.repository.MaatReferenceRepository;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.contribution.repository.ContributionsRepository;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.integration.MockNewWorkReasonRepository;
import gov.uk.courtdata.repository.AppealTypeRepository;
import gov.uk.courtdata.repository.CaseRepository;
import gov.uk.courtdata.repository.ChildWeightHistoryRepository;
import gov.uk.courtdata.repository.ChildWeightingsRepository;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContribCalcParametersRepository;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.repository.CorrespondenceRepository;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;
import gov.uk.courtdata.repository.CrownCourtCodeRepository;
import gov.uk.courtdata.repository.CrownCourtOutcomeRepository;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.DefendantRepository;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.repository.FdcItemsRepository;
import gov.uk.courtdata.repository.FeatureToggleRepository;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsHistoryRepository;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsRepository;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.FinancialAssessmentsHistoryRepository;
import gov.uk.courtdata.repository.HardshipReviewDetailRepository;
import gov.uk.courtdata.repository.HardshipReviewProgressRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.IOJAppealRepository;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import gov.uk.courtdata.repository.PleaRepository;
import gov.uk.courtdata.repository.ProceedingRepository;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.RepOrderCapitalRepository;
import gov.uk.courtdata.repository.RepOrderMvoRegRepository;
import gov.uk.courtdata.repository.RepOrderMvoRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.repository.ReservationsRepository;
import gov.uk.courtdata.repository.ResultRepository;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleDataItemsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.repository.SessionRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.SolicitorRepository;
import gov.uk.courtdata.repository.UnlinkReasonRepository;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.repository.UserRolesRepository;
import gov.uk.courtdata.repository.VerdictRepository;
import gov.uk.courtdata.repository.WQCaseRepository;
import gov.uk.courtdata.repository.WQDefendantRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import gov.uk.courtdata.repository.WQOffenceRepository;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.WQSessionRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.repository.XLATOffenceRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class Repositories {

  @Autowired
  public AppealTypeRepository appealType;

  @Autowired
  public ApplicantDisabilitiesRepository applicantDisabilities;

  @Autowired
  public ApplicantHistoryRepository applicantHistory;

  @Autowired
  public CaseRepository caseRepository;

  @Autowired
  public ChildWeightHistoryRepository childWeightHistory;

  @Autowired
  public ChildWeightingsRepository childWeightings;

  @Autowired
  public ConcorContributionsRepository concorContributions;

  @Autowired
  public ContribCalcParametersRepository contribCalcParameters;

  @Autowired
  public ContributionFilesRepository contributionFiles;

  @Autowired
  public ContributionFileErrorsRepository contributionFileErrors;

  @Autowired
  public ContributionsRepository contributions;

  @Autowired
  public CorrespondenceRepository correspondence;

  @Autowired
  public CourtHouseCodesRepository courtHouseCodes;

  @Autowired
  public CrownCourtCodeRepository crownCourtCode;

  @Autowired
  public CrownCourtOutcomeRepository crownCourtOutcome;

  @Autowired
  public CrownCourtProcessingRepository crownCourtProcessing;

  @Autowired
  public DefendantMAATDataRepository defendantMAATData;

  @Autowired
  public DefendantRepository defendant;

  @Autowired
  public EformStagingRepository eformStaging;

  @Autowired
  public FdcContributionsRepository fdcContributions;

  @Autowired
  public FdcItemsRepository fdcItemsRepository;

  @Autowired
  public FeatureToggleRepository featureToggleRepository;

  @Autowired
  public FinancialAssessmentDetailsHistoryRepository financialAssessmentDetailsHistory;

  @Autowired
  public FinancialAssessmentDetailsRepository financialAssessmentDetails;

  @Autowired
  public FinancialAssessmentRepository financialAssessment;

  @Autowired
  public FinancialAssessmentsHistoryRepository financialAssessmentsHistory;

  @Autowired
  public HardshipReviewDetailRepository hardshipReviewDetail;

  @Autowired
  public HardshipReviewProgressRepository hardshipReviewProgress;

  @Autowired
  public HardshipReviewRepository hardshipReview;

  @Autowired
  public IdentifierRepository identifier;

  @Autowired
  public IOJAppealRepository iojAppeal;

  @Autowired
  public MockNewWorkReasonRepository mockNewWorkReason;

  @Autowired
  public OffenceRepository offence;

  @Autowired
  public PassportAssessmentRepository passportAssessment;

  @Autowired
  public PleaRepository plea;

  @Autowired
  public ProceedingRepository proceeding;

  @Autowired
  public QueueMessageLogRepository queueMessageLog;

  @Autowired
  public RepOrderApplicantLinksRepository repOrderApplicantLinks;

  @Autowired
  public RepOrderCapitalRepository repOrderCapital;

  @Autowired
  public RepOrderCPDataRepository repOrderCPData;

  @Autowired
  public RepOrderMvoRegRepository repOrderMvoReg;

  @Autowired
  public RepOrderMvoRepository repOrderMvo;

  @Autowired
  public RepOrderRepository repOrder;

  @Autowired
  public ReservationsRepository reservations;

  @Autowired
  public ResultRepository result;

  @Autowired
  public RoleActionsRepository roleActions;

  @Autowired
  public RoleDataItemsRepository roleDataItems;

  @Autowired
  public RoleWorkReasonsRepository roleWorkReasons;

  @Autowired
  public SessionRepository session;

  @Autowired
  public SolicitorMAATDataRepository solicitorMAATData;

  @Autowired
  public SolicitorRepository solicitor;

  @Autowired
  public UnlinkReasonRepository unlinkReason;

  @Autowired
  public UserRepository user;

  @Autowired
  public UserRolesRepository userRoles;

  @Autowired
  public VerdictRepository verdict;

  @Autowired
  public WQCaseRepository wqCase;

  @Autowired
  public WqCoreRepository wqCore;

  @Autowired
  public WQDefendantRepository wqDefendant;

  @Autowired
  public WQHearingRepository wqHearing;

  @Autowired
  public WqLinkRegisterRepository wqLinkRegister;

  @Autowired
  public WQOffenceRepository wqOffence;

  @Autowired
  public WQResultRepository wqResult;

  @Autowired
  public WQSessionRepository wqSession;

  @Autowired
  public XLATOffenceRepository xlatOffence;

  @Autowired
  public XLATResultRepository xlatResult;
  
  @Autowired
  public MaatReferenceRepository maatReference;
  
  @Autowired
  private RepositoryUtil repositoryUtil;


  @NotNull
  private JpaRepository[] allRepositories() {
    return new JpaRepository[]{appealType,
        applicantDisabilities,
        applicantHistory,
        caseRepository,
        childWeightHistory,
        childWeightings,
        concorContributions,
        contribCalcParameters,
        contributionFiles,
        contributionFileErrors,
        contributions,
        correspondence,
        courtHouseCodes,
        crownCourtCode,
        crownCourtOutcome,
        crownCourtProcessing,
        defendantMAATData,
        defendant,
        eformStaging,
        fdcContributions,
        featureToggleRepository,
        financialAssessmentDetailsHistory,
        financialAssessmentDetails,
        financialAssessment,
        financialAssessmentsHistory,
        hardshipReviewDetail,
        hardshipReviewProgress,
        hardshipReview,
        identifier,
        iojAppeal,
        mockNewWorkReason,
        offence,
        passportAssessment,
        plea,
        proceeding,
        queueMessageLog,
        repOrderApplicantLinks,
        repOrderCapital,
        repOrderCPData,
        repOrderMvoReg,
        repOrderMvo,
        repOrder,
        reservations,
        result,
        roleActions,
        roleDataItems,
        roleWorkReasons,
        session,
        solicitorMAATData,
        solicitor,
        unlinkReason,
        user,
        userRoles,
        verdict,
        wqCase,
        wqCore,
        wqDefendant,
        wqHearing,
        wqLinkRegister,
        wqOffence,
        wqResult,
        wqSession,
        xlatOffence,
        fdcItemsRepository,
        xlatResult,
        maatReference};
  }

  public void insertCommonTestData() {
    user.save(UserEntity.builder().username(TestEntityDataBuilder.TEST_USER).build());
    user.save(UserEntity.builder().username(TestEntityDataBuilder.USER_CREATED_TEST_S).build());
  }

  public void clearAll() {
    repositoryUtil.clearUp(allRepositories());
  }
}
