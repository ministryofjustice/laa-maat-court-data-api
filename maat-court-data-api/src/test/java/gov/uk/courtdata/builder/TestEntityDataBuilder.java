package gov.uk.courtdata.builder;

import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailReason;
import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import gov.uk.courtdata.reporder.projection.RepOrderEntityInfo;
import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;
import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.enums.HardshipReviewDetailType;
import uk.gov.justice.laa.crime.enums.HardshipReviewStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.REGISTRATION;
import static gov.uk.courtdata.builder.TestModelDataBuilder.USER_NAME;

@Component
public class TestEntityDataBuilder {

    public static final Integer REP_ID = 1234;
    public static final Integer MVO_ID = 5678;
    public static final String TEST_REGISTRATION = "SD51ZDW";
    public static final String TEST_USER = "test-f";
    public static final String USER_CREATED_TEST_S = "test-s";
    public static final String ASSESSOR_USER_NAME = "grea-k";
    public static final String ASSESSMENT_TYPE = "INIT";
    public static final LocalDateTime TEST_DATE = LocalDateTime.of(2022, 1, 1, 0, 0);
    public static final Integer TEST_CASE_ID = 665313;
    public static final String TEST_OFFENCE_ID = "634169aa-265b-4bb5-a7b0-04718f896d2f";
    public static final String TEST_ASN_SEQ = "123";
    public static final Integer APPLICANT_ID = 2345;

    public static RepOrderEntity getRepOrder() {
        return RepOrderEntity.builder().id(REP_ID).build();
    }

    public static RepOrderEntity getPopulatedRepOrder(Integer id) {
        return RepOrderEntity.builder()
                .id(id)
                .catyCaseType("case-type")
                .magsOutcome("outcome")
                .magsOutcomeDate(TEST_DATE.toString())
                .magsOutcomeDateSet(TEST_DATE)
                .committalDate(TEST_DATE.toLocalDate())
                .decisionReasonCode("rder-code")
                .crownRepOrderDecision("cc-rep-doc")
                .crownRepOrderType("cc-rep-type")
                .sentenceOrderDate(TEST_DATE.toLocalDate())
                .build();
    }

    public static RepOrderEntity getPopulatedRepOrder() {
        return RepOrderEntity.builder()
                .catyCaseType("case-type")
                .magsOutcome("outcome")
                .magsOutcomeDate(TEST_DATE.toString())
                .magsOutcomeDateSet(TEST_DATE)
                .committalDate(TEST_DATE.toLocalDate())
                .decisionReasonCode("rder-code")
                .crownRepOrderDecision("cc-rep-doc")
                .crownRepOrderType("cc-rep-type")
                .sentenceOrderDate(TEST_DATE.toLocalDate())
                .build();
    }

    public static RepOrderEntity getPopulatedRepOrder(LocalDate sentenceOrderDate, LocalDate dateReceived) {
        return RepOrderEntity.builder()
                .catyCaseType("case-type")
                .magsOutcome("outcome")
                .magsOutcomeDate(TEST_DATE.toString())
                .magsOutcomeDateSet(TEST_DATE)
                .committalDate(TEST_DATE.toLocalDate())
                .decisionReasonCode("rder-code")
                .crownRepOrderDecision("cc-rep-doc")
                .crownRepOrderType("cc-rep-type")
                .sentenceOrderDate(sentenceOrderDate)
                .dateReceived(dateReceived)
                .build();
    }

    public static ApplicantDisabilitiesEntity getApplicantDisabilitiesEntity() {
        return ApplicantDisabilitiesEntity.builder()
                .applId(5136528)
                .userCreated("TEST-S")
                .userModified("TEST-M")
                .dateCreated(LocalDateTime.parse("2023-10-09T15:02:25"))
                .dateModified(LocalDateTime.parse("2023-10-09T15:02:25"))
                .disaDisability("NO COMMENT")
                .build();
    }

    public static RepOrderMvoEntity getRepOrderMvoEntity(Integer id, RepOrderEntity repOrderEntity) {
        return RepOrderMvoEntity.builder()
                .id(id)
                .rep(repOrderEntity)
                .vehicleOwner("Y")
                .dateCreated(TEST_DATE)
                .userCreated(TEST_USER)
                .dateModified(TEST_DATE)
                .userModified(TEST_USER)
                .build();
    }


    public static RepOrderMvoRegEntity getRepOrderMvoRegEntity(Integer id, RepOrderEntity repOrder) {
        return RepOrderMvoRegEntity.builder()
                .id(id)
                .mvo(getRepOrderMvoEntity(MVO_ID, repOrder))
                .registration(TEST_REGISTRATION)
                .dateCreated(TEST_DATE)
                .userCreated(TEST_USER)
                .dateModified(TEST_DATE)
                .userModified(TEST_USER)
                .build();
    }

    public static RepOrderMvoEntityInfo getRepOrderMvoEntityInfo() {
        return new RepOrderMvoEntityInfo() {
            public Integer getId() {
                return MVO_ID;
            }

            public String getVehicleOwner() {
                return "Y";
            }

            public RepOrderEntityInfo getRep() {
                return getRepOrderEntityInfo();
            }

        };
    }


    public static RepOrderEntityInfo getRepOrderEntityInfo() {
        return new RepOrderEntityInfo() {
            public Integer getId() {
                return REP_ID;
            }

            public LocalDate getDateCreated() {
                return LocalDate.now();
            }
        };
    }


    public static List<RepOrderMvoRegEntityInfo> getRepOrderMvoRegEntityInfo() {
        return List.of(new RepOrderMvoRegEntityInfo() {
            public Integer getId() {
                return REP_ID;
            }

            public LocalDate getDateDeleted() {
                return null;
            }

            public String getRegistration() {
                return REGISTRATION;
            }

        });
    }


    public static FinancialAssessmentEntity getFinancialAssessmentEntity() {
        return FinancialAssessmentEntity.builder()
                .id(1000)
                .repOrder(getPopulatedRepOrder(REP_ID))
                .assessmentType(ASSESSMENT_TYPE)
                .initialAscrId(1)
                .newWorkReason(
                        NewWorkReasonEntity.builder()
                                .code("FMA")
                                .build()
                )
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER)
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00).setScale(2))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00).setScale(2))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .userModified(TEST_USER)
                .firstReminderDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .secondReminderDate(LocalDateTime.parse("2022-10-09T15:02:25"))
                .evidenceReceivedDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .build();
    }

    public static FinancialAssessmentEntity getCustomFinancialAssessmentEntity(int repId,
                                                                               String assessmentStatus,
                                                                               NewWorkReasonEntity newWorkReason) {
        FinancialAssessmentEntity financialAssessmentEntity = FinancialAssessmentEntity.builder()
                .repOrder(getPopulatedRepOrder(repId))
                .assessmentType(ASSESSMENT_TYPE)
                .fassInitStatus(assessmentStatus)
                .dateCreated(TEST_DATE)
                .userCreated(TEST_USER)
                .initialAscrId(1)
                .usn(2)
                .cmuId(3)
                .replaced("N")
                .newWorkReason(newWorkReason)
                .userCreatedEntity(getUserEntity(TEST_USER))
                .build();
        financialAssessmentEntity.addFinAssIncomeEvidences(getFinAssIncomeEvidenceEntity());
        FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity = getFinAssIncomeEvidenceEntity();
        finAssIncomeEvidenceEntity.setIncomeEvidence("NINO");
        financialAssessmentEntity.addFinAssIncomeEvidences(finAssIncomeEvidenceEntity);
        return financialAssessmentEntity;
    }

    public static FinancialAssessmentEntity getFinancialAssessmentEntityWithDetails() {
        FinancialAssessmentEntity financialAssessment = getFinancialAssessmentEntity();
        financialAssessment.addAssessmentDetail(getFinancialAssessmentDetailsEntity());
        return financialAssessment;
    }

    public static FinancialAssessmentEntity getFinancialAssessmentEntityWithChildWeightings() {
        FinancialAssessmentEntity financialAssessment = getFinancialAssessmentEntity();
        financialAssessment.addChildWeighting(getChildWeightingsEntity());
        return financialAssessment;
    }

    public static FinancialAssessmentEntity getFinancialAssessmentEntityWithIncomeEvidence() {
        FinancialAssessmentEntity financialAssessment = getFinancialAssessmentEntity();
        financialAssessment.setId(100);
        financialAssessment.addFinAssIncomeEvidences(getFinAssIncomeEvidenceEntity());
        return financialAssessment;
    }

    public static FinancialAssessmentEntity getFinancialAssessmentEntityWithRelationships(Integer repId,
                                                                                          NewWorkReasonEntity newWorkReason) {
        FinancialAssessmentEntity financialAssessment = getFinancialAssessmentEntity();
        financialAssessment.setId(null);
        financialAssessment.getRepOrder().setId(repId);
        financialAssessment.setNewWorkReason(newWorkReason);
        FinancialAssessmentDetailEntity details = getFinancialAssessmentDetailsEntity();
        details.setId(null);
        financialAssessment.addAssessmentDetail(details);
        financialAssessment.addChildWeighting(getChildWeightingsEntity());
        financialAssessment.addFinAssIncomeEvidences(getFinAssIncomeEvidenceEntity());
        return financialAssessment;
    }

    public static FinancialAssessmentDetailEntity getFinancialAssessmentDetailsEntity() {
        return FinancialAssessmentDetailEntity.builder()
                .id(23456).criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00).setScale(2))
                .applicantFrequency(Frequency.MONTHLY)
                .partnerAmount(BigDecimal.valueOf(1650.00).setScale(2))
                .partnerFrequency(Frequency.TWO_WEEKLY)
                .userCreated(TEST_USER)
                .userModified(TEST_USER)
                .build();
    }

    public static IOJAppealEntity getIOJAppealEntity(LocalDateTime dateModified, String iapStatus) {
        return IOJAppealEntity.builder()
                .id(IOJ_APPEAL_ID)
                .repOrder(getPopulatedRepOrder(IOJ_REP_ID))
                .appealSetupDate(LocalDateTime.of(2022, 1, 1, 10, 0))
                .nworCode("NEW")
                .userCreated(USER_CREATED_TEST_S)
                .cmuId(86679086)
                .iapsStatus(StringUtils.isBlank(iapStatus) ? "COMPLETE" : iapStatus)
                .appealSetupResult("GRANT")
                .iderCode("NOTUNDPROC")
                .decisionDate(LocalDateTime.of(2022, 1, 1, 10, 0))
                .decisionResult("PASS")
                .notes("notes test notes")
                .replaced("N")
                .dateModified(dateModified)
                .build();
    }

    public static IOJAppealEntity getIOJAppealEntity() {
        return getIOJAppealEntity(null, null);
    }

    public static IOJAppealEntity getIOJAppealEntity(LocalDateTime dateModified) {
        return getIOJAppealEntity(dateModified, null);
    }

    public static IOJAppealEntity getIOJAppealEntity(String iapStatus) {
        return getIOJAppealEntity(null, iapStatus);
    }

    public static PassportAssessmentEntity getPassportAssessmentEntity() {
        return PassportAssessmentEntity.builder()
                .id(1000)
                .repOrder(getPopulatedRepOrder(REP_ID))
                .nworCode("FMA")
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER).cmuId(30)
                .assessmentDate(LocalDateTime.parse("2021-10-09T15:01:25"))
                .partnerBenefitClaimed("Y")
                .partnerFirstName("Test")
                .partnerSurname("Partner")
                .partnerNiNumber("AB123456C")
                .partnerDob(LocalDateTime.parse("1978-10-09T06:00:00"))
                .incomeSupport("Y")
                .jobSeekers("Y")
                .statePensionCredit("N")
                .under18FullEducation("N")
                .under16("N")
                .pcobConfirmation("DWP")
                .result("PASS")
                .dateModified(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userModified(TEST_USER)
                .dwpResult("Yes")
                .between16And17("N")
                .under18HeardInYouthCourt("N")
                .under18HeardInMagsCourt("N")
                .lastSignOnDate(LocalDateTime.parse("2021-08-09T12:12:48"))
                .esa("N")
                .pastStatus("COMPLETE")
                .replaced("N")
                .valid("Y")
                .dateCompleted(LocalDateTime.parse("2021-10-09T15:01:25"))
                .usn(1234)
                .whoDWPChecked("ABC")
                .rtCode("DEF")
                .build();
    }

    public static HardshipReviewEntity getHardshipReviewEntityWithRelationships() {
        HardshipReviewEntity hardshipReview = getHardshipReviewEntity();
        hardshipReview.addReviewDetail(getHardshipReviewDetailsEntity());
        hardshipReview.addReviewProgressItem(getHardshipReviewProgressEntity());
        return hardshipReview;
    }

    public static HardshipReviewEntity getHardshipReviewEntity() {
        return HardshipReviewEntity.builder()
                .id(1000)
                .repId(REP_ID)
                .newWorkReason(
                        NewWorkReasonEntity.builder()
                                .code("NEW")
                                .type("HARDIOJ")
                                .description("New")
                                .build()
                )
                .cmuId(253)
                .reviewResult("FAIL")
                .solicitorRate(BigDecimal.valueOf(183.0))
                .solicitorHours(BigDecimal.valueOf(12.0))
                .solicitorVat(BigDecimal.valueOf(384.25))
                .solicitorDisb(BigDecimal.valueOf(0.0))
                .solicitorEstTotalCost(BigDecimal.valueOf(2580.25))
                .disposableIncome(BigDecimal.valueOf(4215.46))
                .disposableIncomeAfterHardship(BigDecimal.valueOf(2921.38))
                .status(HardshipReviewStatus.COMPLETE.getStatus())
                .userCreated(USER_CREATED_TEST_S)
                .userModified(USER_CREATED_TEST_S)
                .courtType("MAGISTRATE")
                .financialAssessmentId(349211)
                .valid("Y")
                .build();
    }

    public static HardshipReviewDetailEntity getHardshipReviewDetailsEntity() {
        return HardshipReviewDetailEntity.builder()
                .id(4253)
                .detailType(HardshipReviewDetailType.EXPENDITURE.getType())
                .userCreated(USER_CREATED_TEST_S)
                .frequency(Frequency.MONTHLY)
                .description("Pension")
                .amount(BigDecimal.valueOf(107.84))
                .accepted("Y")
                .reasonResponse("evidence provided")
                .active("false")
                .detailReason(HardshipReviewDetailReason.ALLOWABLE_EXPENSE)
                .build();
    }

    public static HardshipReviewProgressEntity getHardshipReviewProgressEntity() {
        return HardshipReviewProgressEntity.builder()
                .id(1254)
                .userCreated(USER_CREATED_TEST_S)
                .userModified(USER_CREATED_TEST_S)
                .progressAction(HardshipReviewProgressAction.ADDITIONAL_EVIDENCE)
                .progressResponse(HardshipReviewProgressResponse.FURTHER_RECEIVED)
                .build();
    }

    public static NewWorkReasonEntity getNewWorkReasonEntity() {
        return NewWorkReasonEntity.builder()
                .code("NEW")
                .type("HARDIOJ")
                .description("New")
                .dateCreated(LocalDateTime.now())
                .userCreated("TOGDATA")
                .sequence(1)
                .enabled("Y")
                .initialDefault("Y")
                .build();
    }

    public static NewWorkReasonEntity getFmaNewWorkReasonEntity() {
        return NewWorkReasonEntity.builder()
                .code("FMA")
                .type("ASS")
                .description("First Means Assessment")
                .dateCreated(TEST_DATE)
                .userCreated(TEST_USER)
                .build();
    }

    public static RepOrderApplicantLinksEntity getRepOrderApplicantLinksEntity() {
        return RepOrderApplicantLinksEntity.builder()
                .repId(REP_ID)
                .partnerAphiId(11553872)
                .partnerApplId(11553844)
                .linkDate(LocalDate.parse("2021-10-09"))
                .unlinkDate(LocalDate.parse("2021-10-21"))
                .userCreated("test-u")
                .userModified(TEST_USER)
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .dateModified(LocalDateTime.parse("2021-10-21T15:01:25"))
                .build();
    }

    public static ApplicantHistoryEntity getApplicantHistoryEntity(String sendToCclf) {
        return ApplicantHistoryEntity.builder()
                .applId(716)
                .dob(LocalDate.parse("1981-10-14"))
                .bankAccountName("test-acc-name")
                .email("test@test.com")
                .asAtDate(LocalDate.parse("2006-10-06"))
                .firstName("test_first")
                .lastName("test_last")
                .otherNames("test")
                .niNumber("JM933396A")
                .gender("Male")
                .sendToCclf(sendToCclf)
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("TEST")
                .build();
    }

    public static ApplicantHistoryBillingEntity getApplicantHistoryBillingEntity() {
        return ApplicantHistoryBillingEntity.builder()
                .id(666)
                .applId(666)
                .asAtDate(LocalDate.parse("2006-10-06"))
                .firstName("test_first")
                .lastName("test_last")
                .otherNames("test")
                .dob(LocalDate.parse("1981-10-14"))
                .gender("Male")
                .niNumber("JM933396A")
                .foreignId("T35T")
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("TEST")
                .dateModified(null)
                .userModified(null)
                .build();
    }

    public static FinancialAssessmentsHistoryEntity getFinancialAssessmentsHistoryEntity() {
        return FinancialAssessmentsHistoryEntity.builder()
                .id(4321)
                .repId(REP_ID)
                .initialAscrId(1)
                .newWorkReason(getNewWorkReasonEntity())
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER)
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .userModified(TEST_USER)
                .assessmentDetails(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsHistoryEntity()))
                .childWeightings(List.of(TestEntityDataBuilder.getChildWeightHistoryEntity()))
                .build();
    }

    public static FinancialAssessmentDetailsHistoryEntity getFinancialAssessmentDetailsHistoryEntity() {
        return FinancialAssessmentDetailsHistoryEntity.builder()
                .id(23456)
                .criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00))
                .partnerAmount(BigDecimal.valueOf(1650.00))
                .userCreated(TEST_USER)
                .userModified(TEST_USER)
                .build();
    }

    public static ChildWeightingsEntity getChildWeightingsEntity() {
        return ChildWeightingsEntity.builder()
                .noOfChildren(1)
                .childWeightingId(12)
                .build();
    }

    public static ChildWeightHistoryEntity getChildWeightHistoryEntity() {
        return ChildWeightHistoryEntity.builder()
                .noOfChildren(1)
                .childWeightingId(12)
                .build();
    }

    public static FinAssIncomeEvidenceEntity getFinAssIncomeEvidenceEntity() {
        return FinAssIncomeEvidenceEntity.builder()
                .dateReceived(LocalDateTime.parse("2021-10-09T15:01:25"))
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER)
                .userModified(TEST_USER)
                .active("Y")
                .incomeEvidence("INE")
                .build();
    }

    public static OffenceEntity getOffenceEntity(Integer offenceTxId) {

        return OffenceEntity.builder()
                .txId(offenceTxId)
                .caseId(TEST_CASE_ID)
                .asnSeq("001")
                .offenceShortTitle("Robbery")
                .offenceClassification("Classification")
                .offenceWording("Offence Details")
                .modeOfTrial(1)
                .legalAidStatus("GQ")
                .offenceCode("AA06035")
                .offenceId(TEST_OFFENCE_ID)
                .isCCNewOffence("Y")
                .applicationFlag(0)
                .build();
    }

    public static WqLinkRegisterEntity getWQLinkRegisterEntity(Integer createdTxId) {

        return WqLinkRegisterEntity.builder()
                .createdTxId(createdTxId)
                .caseId(TEST_CASE_ID)
                .maatId(REP_ID)
                .cjsAreaCode("16")
                .cjsLocation("B16BG")
                .maatCat(253)
                .createdUserId(TEST_USER)
                .mlrCat(253)
                .caseUrn("52SB0067421")
                .libraId("CP665371")
                .build();
    }

    public static WQOffenceEntity getWQOffenceEntity(Integer offenceTxId) {

        return WQOffenceEntity.builder()
                .txId(offenceTxId)
                .caseId(TEST_CASE_ID)
                .asnSeq("001")
                .offenceShortTitle("Robbery")
                .offenceClassification("Classification")
                .offenceWording("Offence Details")
                .modeOfTrial(1)
                .legalAidStatus("GQ")
                .offenceCode("AA06035")
                .offenceId(TEST_OFFENCE_ID)
                .isCCNewOffence("Y")
                .applicationFlag(0)
                .build();
    }

    public static WQHearingEntity getWQHearingEntity(Integer createdTxId) {
        return WQHearingEntity.builder()
                .txId(createdTxId)
                .caseId(TEST_CASE_ID)
                .maatId(REP_ID)
                .hearingUUID(TEST_OFFENCE_ID)
                .wqJurisdictionType("CROWN")
                .ouCourtLocation("C22SR")
                .caseUrn("EITHERWAY")
                .resultCodes("4028")
                .build();
    }

    public static RepOrderCapitalEntity getRepOrderCapitalEntity(Integer id, Integer repId, String capitalType) {

        return RepOrderCapitalEntity.builder()
                .id(id)
                .repId(repId)
                .captCapitalType(capitalType)
                .active("Y")
                .dateCreated(LocalDateTime.now())
                .userCreated(TEST_USER)
                .dateAllEvidenceReceived(LocalDateTime.now())
                .build();
    }

    public static ContributionsEntity getContributionsEntity() {

        return ContributionsEntity.builder()
                .applicantId(APPLICANT_ID)
                .userCreated(USER_NAME)
                .contributionFileId(1)
                .effectiveDate(TEST_DATE.toLocalDate())
                .calcDate(TEST_DATE.toLocalDate())
                .contributionCap(new BigDecimal(9999))
                .monthlyContributions(new BigDecimal(99))
                .upfrontContributions(new BigDecimal(9))
                .upliftApplied("Y")
                .basedOn("Means")
                .transferStatus("RECEIVED")
                .dateUpliftApplied(TEST_DATE.toLocalDate())
                .dateUpliftRemoved(TEST_DATE.toLocalDate())
                .createContributionOrder("Y")
                .correspondenceId(9)
                .ccOutcomeCount(9)
                .seHistoryId(9)
                .latest(true)
                .build();
    }

    public static ContributionFilesEntity getContributionFilesEntity() {
        return ContributionFilesEntity.builder()
                .fileName("CONTRIBUTIONS_202307111234.xml")
                .recordsSent(0)
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(TEST_USER)
                .build();
    }

    public static ContributionFilesEntity getContributionFilesEntity(String fileName, Integer recordsSent, Integer recordsReceived, boolean isBlankXml) {
        ContributionFilesEntity entity = ContributionFilesEntity.builder()
                .fileName(fileName)
                .recordsSent(recordsSent)
                .recordsReceived(recordsReceived)
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(TEST_USER)
                .build();
        if (!isBlankXml){
            entity.setXmlContent("<xml>"+fileName+"</xml>");
            entity.setAckXmlContent("<ackXml>"+fileName+"</ackXml>");
        }
        return entity;
    }

    public static ContributionFilesEntity getPopulatedContributionFilesEntity(Integer fileId) {
        return getPopulatedContributionFilesEntity(fileId, "CONTRIBUTIONS_202405210958");
    }

    public static ContributionFilesEntity getPopulatedContributionFilesEntity(Integer fileId, String fileName) {
        return getPopulatedContributionFilesEntity(fileId, fileName, "<xml>content</xml>");
    }

    public static ContributionFilesEntity getPopulatedContributionFilesEntity(Integer fileId, String fileName, String xmlContent){
        return ContributionFilesEntity.builder()
                .fileId(fileId)
                .fileName(fileName)
                .recordsSent(53)
                .recordsReceived(42)
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(USER_CREATED_TEST_S)
                .dateModified(TEST_DATE.toLocalDate().plusDays(3))
                .userModified(TEST_USER)
                .xmlContent(xmlContent)
                .dateSent(TEST_DATE.toLocalDate().plusDays(1))
                .dateReceived(TEST_DATE.toLocalDate().plusDays(2))
                .ackXmlContent("<ackXml>content</ackXml>")
                .build();
    }

    public static ContributionFileErrorsEntity getContributionFileErrorsEntity(int fileId, int contributionId) {
        return ContributionFileErrorsEntity.builder()
                .contributionFileId(fileId)
                .contributionId(contributionId)
                .repId(REP_ID)
                .errorText("error")
                .fixAction("action")
                .fdcContributionId(null)
                .concorContributionId(contributionId)
                .dateCreated(TEST_DATE)
                .build();
    }

    public static ConcorContributionsEntity getConcorContributionsEntity(Integer repId, ConcorContributionStatus status) {
        return ConcorContributionsEntity.builder()
                .status(status)
                .repId(repId)
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(TEST_USER)
                .build();
    }

    public static ConcorContributionsEntity getConcorContributionsEntity(Integer repId, ConcorContributionStatus status, Integer fileId, String currentXml) {
        return ConcorContributionsEntity.builder()
                .status(status)
                .repId(repId)
                .contribFileId(fileId)
                .currentXml(currentXml)
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(TEST_USER)
                .build();
    }

    public static ConcorContributionsEntity getPopulatedConcorContributionsEntity(Integer id) {
        return ConcorContributionsEntity.builder()
                .id(id)
                .status(ConcorContributionStatus.ACTIVE)
                .repId(REP_ID)
                .contribFileId(123)
                .currentXml("<xml>currentXml</xml>")
                .fullXml("<xml>fullXml</xml>")
                .ackCode("1")
                .dateCreated(TEST_DATE.toLocalDate())
                .userCreated(TEST_USER)
                .build();
    }

    public static ConcorContributionsEntity getPopulatedConcorContributionsEntity(Integer id, String currentXml) {
        var entity = getPopulatedConcorContributionsEntity(id);
        entity.setCurrentXml(currentXml);
        return entity;
    }

    public static FdcContributionsEntity getPopulatedFdcContributionsEntity(Integer id){
        return FdcContributionsEntity.builder()
                .id(id)
                .status(FdcContributionsStatus.REQUESTED)
                .repOrderEntity(getPopulatedRepOrder())
                .finalCost(BigDecimal.valueOf(111.1))
                .agfsCost(BigDecimal.valueOf(222.2))
                .lgfsCost(BigDecimal.valueOf(333.3))
                .dateCalculated(TEST_DATE.toLocalDate())
                .contFileId(-999)
                .build();
    }

    public static FdcContributionsEntity getPopulatedFdcContributionsEntity(Integer id, Integer repId, Integer fileId){
        return FdcContributionsEntity.builder()
                .id(id)
                .status(FdcContributionsStatus.REQUESTED)
                .repOrderEntity(getPopulatedRepOrder(repId))
                .finalCost(BigDecimal.valueOf(111.1))
                .agfsCost(BigDecimal.valueOf(222.2))
                .lgfsCost(BigDecimal.valueOf(333.3))
                .dateCalculated(TEST_DATE.toLocalDate())
                .contFileId(fileId)
                .build();
    }

    public static FdcContributionsEntity getFdcContibutionsEntity(FdcContributionsStatus status, String finalCost, Integer fileId, RepOrderEntity repOrder) {
        BigDecimal finalCostBigDecimal = new BigDecimal(finalCost);
        return FdcContributionsEntity.builder()
                .status(status)
                .repOrderEntity(repOrder)
                .finalCost(finalCostBigDecimal)
                .agfsCost(finalCostBigDecimal)
                .lgfsCost(finalCostBigDecimal)
                .dateCalculated(TEST_DATE.toLocalDate())
                .contFileId(fileId)
                .build();
    }

    public static RepOrderCCOutComeEntity getRepOrderCCOutcomeEntity(Integer repOderOutComeId, RepOrderEntity repOrder) {
        return RepOrderCCOutComeEntity.builder()
                .repOrder(repOrder)
                .outcome("CONVICTED")
                .userCreated(TEST_USER)
                .caseNumber(TEST_CASE_ID.toString())
                .crownCourtCode("430")
                .outcomeDate(TEST_DATE)
                .id(repOderOutComeId)
                .build();
    }

    public static RepOrderCCOutComeEntity getRepOrderCCOutcomeEntity(RepOrderEntity repOrder, String outcome) {
        return RepOrderCCOutComeEntity.builder()
                .repOrder(repOrder)
                .outcome(outcome)
                .userCreated(TEST_USER)
                .caseNumber(TEST_CASE_ID.toString())
                .crownCourtCode("430")
                .outcomeDate(TEST_DATE)
                .build();
    }

    public static CorrespondenceEntity getCorrespondenceEntity(Integer id) {

        return CorrespondenceEntity.builder()
                .id(id)
                .generateDate(LocalDateTime.now())
                .printDate(LocalDateTime.now())
                .dateCreated(LocalDateTime.now())
                .userCreated(USER_NAME)
                .cotyCorresType("CONTRIBUTION_ORDER")
                .build();
    }

    public static UserEntity getUserEntity() {
        return UserEntity.builder().firstName("Karen")
                .surname("Greaves")
                .username(ASSESSOR_USER_NAME).build();
    }

    public static UserEntity getUserEntity(String username) {
        return UserEntity.builder().firstName("First name of [" + username + "]")
                .surname("Surname of [" + username + "]")
                .username(username).build();
    }

    public RepOrderCPDataEntity getRepOrderEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(REP_ID)
                .caseUrn("caseurn1")
                .dateCreated(LocalDateTime.now())
                .userCreated("user")
                .dateModified(LocalDateTime.now())
                .build();
    }

    public WqLinkRegisterEntity getWqLinkRegisterEntity() {
        return WqLinkRegisterEntity.builder()
                .caseId(345)
                .createdTxId(123)
                .proceedingId(345)
                .cjsAreaCode("16")
                .maatCat(1)
                .mlrCat(1)
                .maatId(REP_ID)
                .build();
    }

    public WqLinkRegisterEntity getWqLinkRegisterEntity(Integer repId) {
        return WqLinkRegisterEntity.builder()
                .caseId(345)
                .createdTxId(123)
                .proceedingId(345)
                .cjsAreaCode("16")
                .maatCat(1)
                .mlrCat(1)
                .maatId(repId)
                .build();
    }

    public DefendantMAATDataEntity getDefendantMAATDataEntity() {
        return DefendantMAATDataEntity.builder()
                .maatId(REP_ID)
                .firstName("T First Name")
                .lastName("T Last Name")
                .libraId("libra id")
                .dob("07041960")
                .build();
    }

    public DefendantMAATDataEntity getDefendantMAATDataEntity(Integer repId) {
        return DefendantMAATDataEntity.builder()
                .maatId(repId)
                .firstName("T First Name")
                .lastName("T Last Name")
                .libraId("libra id")
                .dob("07041960")
                .build();
    }

    public SolicitorMAATDataEntity getSolicitorMAATDataEntity() {
        return SolicitorMAATDataEntity.builder()
                .maatId(REP_ID)
                .accountCode("Acc")
                .accountName("Acc Name")
                .cmuId(123)
                .build();
    }

    public SolicitorMAATDataEntity getSolicitorMAATDataEntity(Integer repId) {
        return SolicitorMAATDataEntity.builder()
                .maatId(repId)
                .accountCode("Acc")
                .accountName("Acc Name")
                .cmuId(123)
                .build();
    }

    public RepOrderCPDataEntity getRepOrderCPDataEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(REP_ID)
                .defendantId("556677")
                .caseUrn("testCaseURN")
                .build();
    }

    public RepOrderCPDataEntity getRepOrderCPDataEntity(Integer repId) {
        return RepOrderCPDataEntity.builder()
                .repOrderId(repId)
                .defendantId("556677")
                .caseUrn("testCaseURN")
                .build();
    }

}
