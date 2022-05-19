package gov.uk.courtdata.builder;

import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;

@Component
public class TestEntityDataBuilder {

    public static RepOrderEntity getRepOrder() {
        return RepOrderEntity.builder()
                .id(1234)
                .dateModified(LocalDateTime.now())
                .build();
    }

    public static FinancialAssessmentEntity getFinancialAssessmentEntity() {
        return FinancialAssessmentEntity.builder()
                .id(1000)
                .repId(5678)
                .initialAscrId(1)
                .newWorkReason(NewWorkReasonEntity.builder().code("FMA").build())
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .userModified("test-f")
                .build();
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

    public static FinancialAssessmentEntity getFinancialAssessmentEntityWithRelationships() {
        FinancialAssessmentEntity financialAssessment = getFinancialAssessmentEntity();
        financialAssessment.addAssessmentDetail(getFinancialAssessmentDetailsEntity());
        financialAssessment.addChildWeighting(getChildWeightingsEntity());
        return financialAssessment;
    }

    public static FinancialAssessmentDetailEntity getFinancialAssessmentDetailsEntity() {
        return FinancialAssessmentDetailEntity.builder()
                .id(23456)
                .criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00))
                .applicantFrequency(Frequency.MONTHLY)
                .partnerAmount(BigDecimal.valueOf(1650.00))
                .partnerFrequency(Frequency.TWO_WEEKLY)
                .userCreated("test-f")
                .userModified("test-f")
                .build();
    }

    public static IOJAppealEntity getIOJAppealEntity(LocalDateTime dateModified, String iapStatus) {
        return IOJAppealEntity.builder()
                .id(IOJ_APPEAL_ID)
                .repId(IOJ_REP_ID)
                .appealSetupDate(LocalDateTime.of(2022, 1, 1, 10, 0))
                .nworCode("NEW")
                .userCreated("test-s")
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
                .repId(5678)
                .nworCode("FMA")
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("test-f")
                .cmuId(30)
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
                .userModified("test-f")
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
                .repId(621580)
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
                .status(HardshipReviewStatus.COMPLETE)
                .userCreated("test-s")
                .userModified("test-s")
                .courtType("MAGISTRATE")
                .financialAssessmentId(349211)
                .valid("Y")
                .build();
    }

    public static HardshipReviewDetailEntity getHardshipReviewDetailsEntity() {
        return HardshipReviewDetailEntity.builder()
                .id(4253)
                .detailType(HardshipReviewDetailType.EXPENDITURE)
                .userCreated("test-s")
                .frequency(Frequency.MONTHLY)
                .description("Pension")
                .amount(BigDecimal.valueOf(107.84))
                .accepted("Y")
                .reasonResponse("evidence provided")
                .active("false")
                .detailReason(HardshipReviewDetailReasonEntity.builder().id(1000).build())
                .build();
    }

    public static HardshipReviewProgressEntity getHardshipReviewProgressEntity() {
        return HardshipReviewProgressEntity.builder()
                .id(1254)
                .userCreated("test-s")
                .userModified("test-s")
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

    public static FinancialAssessmentsHistoryEntity getFinancialAssessmentsHistoryEntity() {
        return FinancialAssessmentsHistoryEntity.builder()
                .id(4321)
                .repId(1234)
                .initialAscrId(1)
                .nworCode("FMA")
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .userModified("test-f")
                .assessmentDetailsList(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsHistoryEntity()))
                .childWeightingsList(List.of(TestEntityDataBuilder.getChildWeightHistoryEntity()))
                .build();
    }

    public static FinancialAssessmentDetailsHistoryEntity getFinancialAssessmentDetailsHistoryEntity() {
        return FinancialAssessmentDetailsHistoryEntity.builder()
                .id(23456)
                .criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00))
                .partnerAmount(BigDecimal.valueOf(1650.00))
                .userCreated("test-f")
                .userModified("test-f")
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

    public RepOrderCPDataEntity getRepOrderEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(1234)
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
                .maatId(1234)
                .build();
    }

    public DefendantMAATDataEntity getDefendantMAATDataEntity() {
        return DefendantMAATDataEntity.builder()
                .maatId(1234)
                .firstName("T First Name")
                .lastName("T Last Name")
                .libraId("libra id")
                .dob("07041960")
                .build();
    }

    public SolicitorMAATDataEntity getSolicitorMAATDataEntity() {
        return SolicitorMAATDataEntity.builder()
                .maatId(1234)
                .accountCode("Acc")
                .accountName("Acc Name")
                .cmuId(123)
                .build();
    }

    public RepOrderCPDataEntity getRepOrderCPDataEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(1234)
                .defendantId("556677")
                .caseUrn("testCaseURN")
                .build();
    }
}
