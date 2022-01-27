package gov.uk.courtdata.builder;

import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;

@Component
public class TestEntityDataBuilder {

    public RepOrderCPDataEntity getRepOrderEntity() {
        return RepOrderCPDataEntity.builder()
                .repOrderId(1234)
                .caseUrn("caseurn1")
                .dateCreated(LocalDateTime.now())
                .userCreated("user")
                .dateModified(LocalDateTime.now())
                .build();
    }

    public RepOrderEntity getRepOrder() {
        return RepOrderEntity.builder()
                .id(1234)
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

    public static FinancialAssessmentEntity getFinancialAssessmentEntity() {
        return FinancialAssessmentEntity.builder()
                .id(1000)
                .repId(5678)
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
                .build();
    }

    public static FinancialAssessmentDetailEntity getFinancialAssessmentDetailsEntity() {
        return FinancialAssessmentDetailEntity.builder()
                .id(23456)
                .criteriaDetailId(40)
                .financialAssessmentId(1000)
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

<<<<<<< HEAD
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
=======
    public static HardshipReviewEntity getHardshipReviewEntity() {
        return HardshipReviewEntity.builder()
                .id(1000)
                .repId(621580)
                .newWorkOrderCode("NEW")
                .caseManagementUnitId(253)
                .reviewResult("FAIL")
                .solicitorRate(183.0)
                .solicitorHours(12.0)
                .solicitorVat(384.25)
                .solicitorDisb(0.0)
                .solicitorEstTotalCost(2580.25)
                .disposableIncome(4215.46)
                .disposableIncomeAfterHardship(2921.38)
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
                .hardshipReviewId(338)
                .type(HardshipReviewDetailType.EXPENDITURE)
                .userCreated("test-s")
                .frequency(Frequency.MONTHLY)
                .description("Pension")
                .amount(BigDecimal.valueOf(107.84))
                .accepted("Y")
                .reasonResponse("evidence provided")
                .active("false")
                .build();
    }

    public static HardshipReviewProgressEntity getHardshipReviewProgressEntity() {
        return HardshipReviewProgressEntity.builder()
                .id(1254)
                .hardshipReviewId(1000)
                .userCreated("test-s")
                .userModified("test-s")
                .progressAction(HardshipReviewProgressAction.ADDITIONAL_EVIDENCE)
                .progressResponse(HardshipReviewProgressResponse.FURTHER_RECEIVED)
>>>>>>> Creation of new endpoint to search for existing hardship reviews
                .build();
    }
}
