package gov.uk.courtdata.integration.util;

import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class Repositories {

    @Autowired
    public AppealTypeRepository appealType;

    @Autowired
    private ApplicantHistoryRepository applicantHistory;

    @Autowired
    public CaseRepository caseRepository;

    @Autowired
    public ChildWeightHistoryRepository childWeightHistory;

    @Autowired
    public ChildWeightingsRepository childWeightings;

    @Autowired
    public ConcorContributionsRepository concorContributions;

    @Autowired
    public ContribAppealRulesRepository contribAppealRules;

    @Autowired
    public ContribCalcParametersRepository contribCalcParameters;

    @Autowired
    public ContributionFilesRepository contributionFiles;

    @Autowired
    public ContributionFileErrorsRepository contributionFileErrors;

    @Autowired
    public CorrespondenceRepository correspondence;

    @Autowired
    public CorrespondenceStateRepository correspondenceState;

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
    private EformStagingRepository eformStaging;

    @Autowired
    public FdcContributionsRepository fdcContributions;

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
    public OffenceRepository offence;

    @Autowired
    public PassportAssessmentRepository passportAssessment;

    @Autowired
    public PleaRepository plea;

    @Autowired
    public ProceedingRepository proceeding;

    @Autowired
    public ProsecutionConcludedRepository prosecutionConcluded;

    @Autowired
    public QueueMessageLogRepository queueMessageLog;

    @Autowired
    private RepOrderApplicantLinksRepository repOrderApplicantLinks;

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
    private RepositoryUtil repositoryUtil;

    @NotNull
    private JpaRepository[] allRepositories() {
        return new JpaRepository[]{appealType,
                applicantHistory,
                caseRepository,
                childWeightHistory,
                childWeightings,
                concorContributions,
                contribAppealRules,
                contribCalcParameters,
                contributionFiles,
                contributionFileErrors,
                correspondence,
                correspondenceState,
                courtHouseCodes,
                crownCourtCode,
                crownCourtOutcome,
                crownCourtProcessing,
                defendantMAATData,
                defendant,
                eformStaging,
                fdcContributions,
                financialAssessmentDetailsHistory,
                financialAssessmentDetails,
                financialAssessment,
                financialAssessmentsHistory,
                hardshipReviewDetail,
                hardshipReviewProgress,
                hardshipReview,
                identifier,
                iojAppeal,
                offence,
                passportAssessment,
                plea,
                proceeding,
                prosecutionConcluded,
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
                xlatResult};
    }

    public void insertCommonTestData() {
        user.save(UserEntity.builder().username(TestEntityDataBuilder.TEST_USER).build());
        user.save(UserEntity.builder().username(TestEntityDataBuilder.USER_CREATED_TEST_S).build());
    }

    public void clearAll() {
        repositoryUtil.clearUp(allRepositories());
    }
}
