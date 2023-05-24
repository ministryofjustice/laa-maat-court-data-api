package gov.uk.courtdata.builder;

import com.google.gson.Gson;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.dto.*;
import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.enums.*;
import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.model.*;
import gov.uk.courtdata.model.assessment.*;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.model.hardship.SolicitorCosts;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestModelDataBuilder {

    public static final UUID HEARING_ID = UUID.randomUUID();
    public static final JurisdictionType JURISDICTION_TYPE_MAGISTRATES = JurisdictionType.MAGISTRATES;
    public static final String COURT_LOCATION = "London";
    public static final Integer IOJ_APPEAL_ID = 123;
    public static final Integer IOJ_REP_ID = 5635978;
    public static final Integer FINANCIAL_ASSESSMENT_ID = 364563;

    public static final Integer REP_ID = 1234;
    public static final Integer MVO_ID = 5678;
    public static final String APP_DATE_COMPLETED = "2022-07-15T15:02:25";
    public static final String TEST_ARREST_SUMMONS_NUMBER = "1000000000000259068J";

    public static final LocalDateTime TEST_DATE = LocalDateTime.of(2022, 1, 1, 0, 0);

    public static final String FINANCIAL_ASSESSMENT_STATUS = "COMPLETE";

    public static final String REGISTRATION = "SD51ZDW";

    public static final String TEST_USER = "test-f";

    public static final Integer TEST_CASE_ID = 665313;

    public static final String TEST_OFFENCE_ID = "634169aa-265b-4bb5-a7b0-04718f896d2f";
    public static final String TEST_ASN_SEQ = "123";
    public static final Integer TEST_RESULT_CODE = 12345;
    public static final String RESERVATION_RECORD_NAME = "REP_ORDER";
    public static final String USER_SESSION = "User Session";
    public static final String USER_NAME = "ONE-T";

    public static final String CASE_TYPE = "APPEAL CC";
    public static final String APTY_CODE = "ASE";
    public static final String OUTCOME = "SUCCESSFUL";
    public static final String ASSESSMENT_RESULT = "PASS";
    public static final BigDecimal CONTRIBUTION_AMOUNT = BigDecimal.valueOf(500.00);
    public static final String CORRESPONDENCE_STATUS = "appealCC";
    public static final String EFFECTIVE_DATE = "01-JAN-20233";

    TestEntityDataBuilder testEntityDataBuilder;
    Gson gson;

    public TestModelDataBuilder(TestEntityDataBuilder testEntityDataBuilder, Gson gson) {
        this.gson = gson;
        this.testEntityDataBuilder = testEntityDataBuilder;
    }

    public static FinancialAssessmentDTO getFinancialAssessmentDTO() {
        return FinancialAssessmentDTO.builder()
                .repId(5678)
                .initialAscrId(1)
                .assessmentType("INIT")
                .newWorkReason(getNewWorkReason())
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .build();
    }

    public static FinancialAssessmentDTO getFinancialAssessmentDTOWithDetails() {
        FinancialAssessmentDTO financialAssessment = getFinancialAssessmentDTO();
        financialAssessment.setAssessmentDetails(List.of(
                FinancialAssessmentDetails.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build()
        ));
        return financialAssessment;
    }

    public static FinancialAssessmentDTO getFinancialAssessmentWithChildWeightings() {
        FinancialAssessmentDTO financialAssessment = getFinancialAssessmentDTO();
        financialAssessment.setChildWeightings(List.of(getChildWeightings()));
        return financialAssessment;
    }

    public static FinancialAssessmentDetails getFinancialAssessmentDetails() {
        return FinancialAssessmentDetails.builder()
                .criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00))
                .applicantFrequency(Frequency.MONTHLY)
                .partnerAmount(BigDecimal.valueOf(1650.00))
                .partnerFrequency(Frequency.TWO_WEEKLY)
                .build();
    }

    public static NewWorkReason getNewWorkReason() {
        return NewWorkReason.builder()
                .code("FMA")
                .build();
    }

    public static CreateFinancialAssessment getCreateFinancialAssessmentWithRelationships() {
        CreateFinancialAssessment financialAssessment = getCreateFinancialAssessment();
        financialAssessment.setAssessmentDetails(
                List.of(getFinancialAssessmentDetails())
        );
        financialAssessment.setChildWeightings(
                List.of(getChildWeightings())
        );
        return financialAssessment;
    }

    public static CreateFinancialAssessment getCreateFinancialAssessment() {
        return CreateFinancialAssessment.builder()
                .repId(4351623)
                .initialAscrId(1)
                .nworCode("FMA")
                .userCreated("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.now())
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .build();
    }

    public static UpdateFinancialAssessment getUpdateFinancialAssessment() {
        return UpdateFinancialAssessment.builder()
                .id(1234)
                .repId(4351623)
                .initialAscrId(1)
                .userModified("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.now())
                .initTotAggregatedIncome(BigDecimal.valueOf(15600.00))
                .initAdjustedIncomeValue(BigDecimal.valueOf(15600.00))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .build();
    }

    public static String getCreateFinancialAssessmentJson() {
        return "{\n" +
                "\"repId\": 4351623,\n" +
                "\"initialAscrId\": 1,\n" +
                "\"nworCode\": \"FMA\",\n" +
                "\"userCreated\": \"test-f\",\n" +
                "\"cmuId\": 30,\n" +
                "\"fassInitStatus\": \"COMPLETE\",\n" +
                "\"initialAssessmentDate\": \"2021-10-09T15:02:25\",\n" +
                "\"initOtherBenefitNote\": null,\n" +
                "\"initOtherIncomeNote\": null,\n" +
                "\"initTotAggregatedIncome\": 15600.0,\n" +
                "\"initAdjustedIncomeValue\": 15600.0,\n" +
                "\"initNotes\": null,\n" +
                "\"initResult\": \"FULL\",\n" +
                "\"initResultReason\": null,\n" +
                "\"incomeEvidenceDueDate\": null,\n" +
                "\"incomeUpliftRemoveDate\": null,\n" +
                "\"incomeUpliftApplyDate\": null,\n" +
                "\"incomeEvidenceNotes\": null,\n" +
                "\"initApplicationEmploymentStatus\": \"NONPASS\",\n" +
                "\"usn\": null,\n" +
                "\"rtCode\": null\n" +
                "}";
    }

    public static String getUpdateFinancialAssessmentJson() {
        return "{\n" +
                "\"id\": 1234,\n" +
                "\"repId\": 5678,\n" +
                "\"initialAscrId\": 1,\n" +
                "\"assessmentType\": \"FULL\",\n" +
                "\"nworCode\": \"FMA\",\n" +
                "\"cmuId\": 30,\n" +
                "\"fassInitStatus\": \"COMPLETE\",\n" +
                "\"initialAssessmentDate\": \"2006-10-09T00:00:00\",\n" +
                "\"initOtherBenefitNote\": null,\n" +
                "\"initOtherIncomeNote\": null,\n" +
                "\"initTotAggregatedIncome\": 15600.0,\n" +
                "\"initAdjustedIncomeValue\": 15600.0,\n" +
                "\"initNotes\": null,\n" +
                "\"initResult\": \"FULL\",\n" +
                "\"initResultReason\": null,\n" +
                "\"incomeEvidenceDueDate\": null,\n" +
                "\"incomeUpliftRemoveDate\": null,\n" +
                "\"incomeUpliftApplyDate\": null,\n" +
                "\"incomeEvidenceNotes\": null,\n" +
                "\"initApplicationEmploymentStatus\": \"NONPASS\",\n" +
                "\"fassFullStatus\": \"COMPLETE\",\n" +
                "\"fullAssessmentDate\": \"2006-10-09T00:00:00\",\n" +
                "\"fullResultReason\": null,\n" +
                "\"fullAssessmentNotes\": null,\n" +
                "\"fullResult\": \"FAIL\",\n" +
                "\"fullAdjustedLivingAllowance\": 5304.0,\n" +
                "\"fullTotalAnnualDisposableIncome\": 15314.0,\n" +
                "\"fullOtherHousingNote\": null,\n" +
                "\"fullTotalAggregatedExpenses\": null,\n" +
                "\"fullAscrId\": 1,\n" +
                "\"dateCompleted\": \"2006-10-09T00:00:00\",\n" +
                "\"userModified\": \"dohe-f\"\n" +
                "}";
    }

    public static String getUpdateContributionsJson() {
        return "{\n" +
                "\"id\": 999,\n" +
                "\"userModified\": \"test\",\n" +
                "\"contributionFileId\": 9,\n" +
                "\"effectiveDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"calcDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"contributionCap\": 9999,\n" +
                "\"monthlyContributions\": 99,\n" +
                "\"upfrontContributions\": 9,\n" +
                "\"upliftApplied\": \"Y\",\n" +
                "\"basedOn\": \"Means\",\n" +
                "\"transferStatus\": \"RECEIVED\",\n" +
                "\"dateUpliftApplied\": \"2023-04-01T00:00:00.00\",\n" +
                "\"dateUpliftRemoved\": \"2023-04-02T00:00:00.00\",\n" +
                "\"createContributionOrder\": \"Y\",\n" +
                "\"correspondenceId\": 9,\n" +
                "\"ccOutcomeCount\": 9,\n" +
                "\"seHistoryId\": 9\n" +
                "}";
    }

    public static String getInvalidUpdateContributionsJson() {
        return "{\n" +
                "\"id\": 999,\n" +
                "\"userModified\": \"test\",\n" +
                "\"contributionFileId\": 9,\n" +
                "\"calcDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"contributionCap\": 9999,\n" +
                "\"monthlyContributions\": 99,\n" +
                "\"upfrontContributions\": 9,\n" +
                "\"upliftApplied\": \"Y\",\n" +
                "\"basedOn\": \"Means\",\n" +
                "\"transferStatus\": \"RECEIVED\",\n" +
                "\"dateUpliftApplied\": \"2023-04-01T00:00:00.00\",\n" +
                "\"dateUpliftRemoved\": \"2023-04-02T00:00:00.00\",\n" +
                "\"createContributionOrder\": \"Y\",\n" +
                "\"correspondenceId\": 9,\n" +
                "\"ccOutcomeCount\": 9,\n" +
                "\"seHistoryId\": 9\n" +
                "}";
    }

    public static String getCreateContributionsJson() {
        return "{\n" +
                "\"repId\": 999,\n" +
                "\"applId\": 999,\n" +
                "\"userCreated\": \"test\",\n" +
                "\"contributionFileId\": 9,\n" +
                "\"effectiveDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"calcDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"contributionCap\": 9999,\n" +
                "\"monthlyContributions\": 99,\n" +
                "\"upfrontContributions\": 9,\n" +
                "\"upliftApplied\": \"Y\",\n" +
                "\"basedOn\": \"Means\",\n" +
                "\"transferStatus\": \"RECEIVED\",\n" +
                "\"dateUpliftApplied\": \"2023-04-01T00:00:00.00\",\n" +
                "\"dateUpliftRemoved\": \"2023-04-02T00:00:00.00\",\n" +
                "\"createContributionOrder\": \"Y\",\n" +
                "\"correspondenceId\": 9,\n" +
                "\"ccOutcomeCount\": 9,\n" +
                "\"seHistoryId\": 9\n" +
                "}";
    }

    public static String getInvalidCreateContributionsJson() {
        return "{\n" +
                "\"repId\": 999,\n" +
                "\"applId\": 999,\n" +
                "\"userCreated\": \"\",\n" +
                "\"contributionFileId\": 9,\n" +
                "\"effectiveDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"calcDate\": \"2023-04-01T00:00:00.00\",\n" +
                "\"contributionCap\": 9999,\n" +
                "\"monthlyContributions\": 99,\n" +
                "\"upfrontContributions\": 9,\n" +
                "\"upliftApplied\": \"Y\",\n" +
                "\"basedOn\": \"Means\",\n" +
                "\"transferStatus\": \"RECEIVED\",\n" +
                "\"dateUpliftApplied\": \"2023-04-01T00:00:00.00\",\n" +
                "\"dateUpliftRemoved\": \"2023-04-02T00:00:00.00\",\n" +
                "\"createContributionOrder\": \"Y\",\n" +
                "\"correspondenceId\": 9,\n" +
                "\"ccOutcomeCount\": 9,\n" +
                "\"seHistoryId\": 9\n" +
                "}";
    }

    public static IOJAppealDTO getIOJAppealDTO(LocalDateTime dateModified) {
        return IOJAppealDTO.builder()
                .id(IOJ_APPEAL_ID)
                .repId(IOJ_REP_ID)
                .appealSetupDate(getIOJTestDate())
                .nworCode("NEW")
                .userCreated("test-s")
                .cmuId(86679086)
                .iapsStatus("COMPLETE")
                .appealSetupResult("GRANT")
                .iderCode("NOTUNDPROC")
                .decisionDate(getIOJTestDate())
                .decisionResult("PASS")
                .notes("notes test notes")
                .replaced("N")
                .dateModified(dateModified)
                .build();
    }

    public static IOJAppealDTO getIOJAppealDTO() {
        return getIOJAppealDTO(null);
    }

    public static CreateIOJAppeal getCreateIOJAppealObject(boolean isValid) {
        return CreateIOJAppeal.builder()
                .repId(isValid ? IOJ_REP_ID : null)
                .appealSetupDate(getIOJTestDate())
                .nworCode("NEW")
                .userCreated("test-s")
                .cmuId(86679086)
                .iapsStatus("COMPLETE")
                .appealSetupResult("GRANT")
                .iderCode("NOTUNDPROC")
                .decisionDate(getIOJTestDate())
                .decisionResult("PASS")
                .notes("notes test notes")
                .build();
    }

    public static CreateIOJAppeal getCreateIOJAppealObject() {
        return getCreateIOJAppealObject(true);
    }

    public static UpdateIOJAppeal getUpdateIOJAppealObject() {
        return getUpdateIOJAppealObject(true, null);
    }

    public static UpdateIOJAppeal getUpdateIOJAppealObject(LocalDateTime dateModified) {
        return getUpdateIOJAppealObject(true, dateModified);
    }

    public static UpdateIOJAppeal getUpdateIOJAppealObject(boolean isValid) {
        return getUpdateIOJAppealObject(isValid, null);
    }

    public static UpdateIOJAppeal getUpdateIOJAppealObject(boolean isValid, LocalDateTime dateModified) {
        return UpdateIOJAppeal.builder()
                .id(IOJ_APPEAL_ID)
                .repId(isValid ? IOJ_REP_ID : null)
                .appealSetupDate(getIOJTestDate())
                .nworCode("NEW")
                .cmuId(86679086)
                .iapsStatus("IN PROGRESS")
                .appealSetupResult("GRANT")
                .iderCode("NOTUNDPROC")
                .decisionDate(getIOJTestDate())
                .decisionResult("PASS")
                .notes("notes Update test notes")
                .userModified("test-s")
                .dateModified(dateModified)
                .build();
    }

    private static LocalDateTime getIOJTestDate() {
        return LocalDateTime.of(2022, 1, 1, 10, 0);
    }

    public static UserReservation getUserReservation() {
        return UserReservation.builder()
                .reservationId(1000000)
                .session(UserSession.builder()
                        .id("test-f6E3E618A32AC870D07A65CD7AB9131AD")
                        .username("test-f")
                        .build()
                ).build();
    }

    public static String getCreatePassportAssessmentJson() {
        return "{\n" +
                "\"financialAssessmentId\": \"2000\",\n" +
                "\"repId\": \"1234567\",\n" +
                "\"nworCode\": \"FMA\",\n" +
                "\"dateCreated\": \"2021-10-09T15:02:25\",\n" +
                "\"userCreated\": \"test-f\",\n" +
                "\"cmuId\": 30,\n" +
                "\"assessmentDate\": \"2021-10-09T15:02:25\",\n" +
                "\"partnerBenefitClaimed\": \"Y\",\n" +
                "\"partnerFirstName\": \"Test\",\n" +
                "\"partnerSurname\": \"Partner\",\n" +
                "\"partnerNiNumber\": \"AB123456C\",\n" +
                "\"partnerDob\": \"1978-10-09T06:00:00\",\n" +
                "\"incomeSupport\": \"Y\",\n" +
                "\"jobSeekers\": \"Y\",\n" +
                "\"statePensionCredit\": \"N\",\n" +
                "\"under18FullEducation\": \"N\",\n" +
                "\"under16\": \"N\",\n" +
                "\"pcobConfirmation\": \"AGEREL\",\n" +
                "\"result\": \"PASS\",\n" +
                "\"dateModified\": \"2021-10-09T15:01:25\",\n" +
                "\"userModified\": \"test-f\",\n" +
                "\"dwpResult\": \"Yes\",\n" +
                "\"between16And17\": \"N\",\n" +
                "\"under18HeardInYouthCourt\": \"N\",\n" +
                "\"under18HeardInMagsCourt\": \"N\",\n" +
                "\"lastSignOnDate\": \"2021-08-09T12:12:48\",\n" +
                "\"esa\": \"N\",\n" +
                "\"pastStatus\": \"COMPLETE\",\n" +
                "\"replaced\": \"N\",\n" +
                "\"valid\": \"Y\",\n" +
                "\"dateCompleted\": \"2021-10-09T15:02:25\",\n" +
                "\"usn\": \"1234\",\n" +
                "\"whoDWPChecked\": \"ABC\",\n" +
                "\"rtCode\": \"DEF\"\n" +
                "}";
    }

    public static String getUpdatePassportAssessmentJson() {
        return "{\n" +
                "\"id\": \"1000\",\n" +
                "\"repId\": \"1234567\",\n" +
                "\"nworCode\": \"FMA\",\n" +
                "\"cmuId\": 30,\n" +
                "\"assessmentDate\": \"2021-10-09T15:02:25\",\n" +
                "\"partnerBenefitClaimed\": \"Y\",\n" +
                "\"partnerFirstName\": \"Test\",\n" +
                "\"partnerSurname\": \"Partner\",\n" +
                "\"partnerNiNumber\": \"AB123456C\",\n" +
                "\"partnerDob\": \"1978-10-09T06:00:00\",\n" +
                "\"incomeSupport\": \"Y\",\n" +
                "\"jobSeekers\": \"Y\",\n" +
                "\"statePensionCredit\": \"N\",\n" +
                "\"under18FullEducation\": \"N\",\n" +
                "\"under16\": \"N\",\n" +
                "\"pcobConfirmation\": \"DWP\",\n" +
                "\"result\": \"PASS\",\n" +
                "\"dwpResult\": \"Yes\",\n" +
                "\"between16And17\": \"N\",\n" +
                "\"under18HeardInYouthCourt\": \"N\",\n" +
                "\"under18HeardInMagsCourt\": \"N\",\n" +
                "\"lastSignOnDate\": \"2021-08-09T12:12:48\",\n" +
                "\"esa\": \"N\",\n" +
                "\"pastStatus\": \"COMPLETE\",\n" +
                "\"replaced\": \"N\",\n" +
                "\"valid\": \"Y\",\n" +
                "\"dateCompleted\": \"2021-10-09T15:02:25\",\n" +
                "\"whoDWPChecked\": \"ABC\",\n" +
                "\"userModified\": \"test-f\"\n" +
                "}";
    }

    public static PassportAssessmentDTO getPassportAssessmentDTO() {
        return PassportAssessmentDTO.builder()
                .repId(REP_ID)
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

    public static CreatePassportAssessment getCreatePassportAssessment() {
        return CreatePassportAssessment.builder()
                .financialAssessmentId(2000)
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
                .dwpResult("Yes")
                .between16And17("N")
                .under18HeardInYouthCourt("N")
                .under18HeardInMagsCourt("N")
                .lastSignOnDate(LocalDateTime.parse("2021-08-09T12:12:48"))
                .esa("N")
                .pastStatus("COMPLETE")
                .valid("Y")
                .dateCompleted(LocalDateTime.parse("2021-10-09T15:01:25"))
                .usn(1234)
                .whoDWPChecked("ABC")
                .rtCode("DEF")
                .build();
    }

    public static UpdatePassportAssessment getUpdatePassportAssessment() {
        return UpdatePassportAssessment.builder()
                .repId(5678)
                .nworCode("FMA")
                .userModified("test-f")
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
                .dwpResult("Yes")
                .between16And17("N")
                .under18HeardInYouthCourt("N")
                .under18HeardInMagsCourt("N")
                .lastSignOnDate(LocalDateTime.parse("2021-08-09T12:12:48"))
                .esa("N")
                .pastStatus("COMPLETE")
                .valid("Y")
                .dateCompleted(LocalDateTime.parse("2021-10-09T15:01:25"))
                .whoDWPChecked("ABC")
                .build();
    }

    public static HardshipReviewDTO getHardshipReviewDTO() {
        return HardshipReviewDTO.builder()
                .id(1000)
                .repId(621580)
                .newWorkReason(
                        NewWorkReason.builder()
                                .code("NEW")
                                .type("HARDIOJ")
                                .description("New")
                                .build()
                )
                .cmuId(253)
                .reviewResult("FAIL")
                .solicitorCosts(
                        SolicitorCosts.builder()
                                .solicitorRate(DataBuilderUtil.createScaledBigDecimal(183.00))
                                .solicitorHours(DataBuilderUtil.createScaledBigDecimal(12.00))
                                .solicitorVat(DataBuilderUtil.createScaledBigDecimal(384.25))
                                .solicitorDisb(DataBuilderUtil.createScaledBigDecimal(0.00))
                                .solicitorEstTotalCost(DataBuilderUtil.createScaledBigDecimal(2580.25))
                                .build()
                )
                .disposableIncome(DataBuilderUtil.createScaledBigDecimal(4215.46))
                .disposableIncomeAfterHardship(DataBuilderUtil.createScaledBigDecimal(2921.38))
                .status(HardshipReviewStatus.COMPLETE)
                .userCreated("test-s")
                .userModified("test-s")
                .courtType("MAGISTRATE")
                .financialAssessmentId(349211)
                .valid("Y")
                .build();
    }

    public static String getCreateHardshipReviewJson(boolean withRelationships) {
        String json =
                "{\n" +
                        "\"nworCode\":\"NEW\",\n" +
                        "\"cmuId\": 253,\n" +
                        "\"reviewResult\": \"FAIL\",\n" +
                        "\"resultDate\": \"2022-01-01T10:00:00\",\n" +
                        "\"reviewDate\": \"2022-01-01T10:00:00\",\n" +
                        "\"notes\": \"\",\n" +
                        "\"decisionNotes\": \"\",\n" +
                        "\"solicitorCosts\": {\n" +
                        "   \"solicitorRate\": 183.0,\n" +
                        "   \"solicitorHours\": 12.0,\n" +
                        "   \"solicitorVat\": 384.25,\n" +
                        "   \"solicitorDisb\": 0.0,\n" +
                        "   \"solicitorEstTotalCost\": 2580.25\n" +
                        "},\n" +
                        "\"disposableIncome\": 4215.46,\n" +
                        "\"disposableIncomeAfterHardship\": 2921.38,\n" +
                        "\"status\": \"COMPLETE\",\n" +
                        "\"repId\": 621580,\n" +
                        "\"userCreated\": \"test-s\",\n" +
                        "\"courtType\": \"MAGISTRATE\",\n" +
                        "\"financialAssessmentId\": 349211\n";

        if (withRelationships) {
            json = json +
                    ",\"reviewDetails\": [{\n" +
                    "   \"frequency\": \"MONTHLY\",\n" +
                    "   \"amount\": 107.84,\n" +
                    "   \"accepted\": \"Y\",\n" +
                    "   \"type\": \"EXPENDITURE\"\n" +
                    "}],\n" +
                    "\"reviewProgressItems\": [{\n" +
                    "   \"progressAction\": \"ADDITIONAL_EVIDENCE\",\n" +
                    "   \"progressResponse\": \"FURTHER_RECEIVED\"\n" +
                    "}]\n";
        }

        return json + "}";
    }

    public static String getUpdateHardshipReviewJson(boolean withRelationships) {
        String json =
                "{\n" +
                        "\"id\": 1000,\n" +
                        "\"userModified\": \"test-s\",\n" +
                        "\"updated\": \"2022-01-01T10:00:00\",\n" +
                        "\"nworCode\":\"NEW\",\n" +
                        "\"cmuId\": 253,\n" +
                        "\"reviewResult\": \"FAIL\",\n" +
                        "\"resultDate\": \"2022-01-01T10:00:00\",\n" +
                        "\"reviewDate\": \"2022-01-01T10:00:00\",\n" +
                        "\"notes\": \"\",\n" +
                        "\"decisionNotes\": \"\",\n" +
                        "\"solicitorCosts\": {\n" +
                        "   \"solicitorRate\": 183.0,\n" +
                        "   \"solicitorHours\": 12.0,\n" +
                        "   \"solicitorVat\": 384.25,\n" +
                        "   \"solicitorDisb\": 0.0,\n" +
                        "   \"solicitorEstTotalCost\": 2580.25\n" +
                        "},\n" +
                        "\"disposableIncome\": 4215.46,\n" +
                        "\"disposableIncomeAfterHardship\": 2921.38,\n" +
                        "\"status\": \"COMPLETE\",\n" +
                        "\"repId\": 621580\n";

        if (withRelationships) {
            json = json +
                    ",\"reviewDetails\": [{\n" +
                    "   \"frequency\": \"MONTHLY\",\n" +
                    "   \"amount\": 107.84,\n" +
                    "   \"accepted\": \"Y\",\n" +
                    "   \"type\": \"EXPENDITURE\"\n" +
                    "}],\n" +
                    "\"reviewProgressItems\": [{\n" +
                    "   \"progressAction\": \"ADDITIONAL_EVIDENCE\",\n" +
                    "   \"progressResponse\": \"FURTHER_RECEIVED\"\n" +
                    "}]\n";
        }

        return json + "}";
    }

    public static RepOrderDTO getRepOrderDTO() {
        return getRepOrderDTO(TestModelDataBuilder.REP_ID);
    }

    public static RepOrderDTO getRepOrderDTO(Integer id) {
        return RepOrderDTO.builder()
                .id(id)
                .catyCaseType("case-type")
                .magsOutcome("outcome")
                .magsOutcomeDate(TEST_DATE.toString())
                .magsOutcomeDateSet(TEST_DATE)
                .committalDate(TEST_DATE.toLocalDate())
                .decisionReasonCode("rder-code")
                .crownRepOrderDecision("cc-rep-doc")
                .crownRepOrderType("cc-rep-type")
                .build();
    }

    public static List<RepOrderMvoRegDTO> getRepOrderMvoRegDTOList() {
        return List.of(getRepOrderMvoRegDTO(TestModelDataBuilder.REP_ID));
    }

    public static RepOrderMvoRegDTO getRepOrderMvoRegDTO() {
        return getRepOrderMvoRegDTO(TestModelDataBuilder.REP_ID);
    }

    public static RepOrderMvoRegDTO getRepOrderMvoRegDTO(Integer id) {
        return RepOrderMvoRegDTO.builder()
                .id(id)
                .registration(REGISTRATION)
                .build();
    }

    public static RepOrderMvoDTO getRepOrderMvoDTO() {
        return getRepOrderMvoDTO(TestModelDataBuilder.MVO_ID);
    }

    public static RepOrderMvoDTO getRepOrderMvoDTO(Integer id) {
        return RepOrderMvoDTO.builder()
                .id(id)
                .rep(RepOrderDTO.builder()
                        .id(REP_ID)
                        .build())
                .vehicleOwner("Y")
                .build();
    }


    public static HardshipReviewDTO getHardshipReviewDTOWithRelationships() {
        HardshipReviewDTO hardship = getHardshipReviewDTO();
        hardship.setReviewDetails(List.of(getHardshipReviewDetail()));
        hardship.setReviewProgressItems(List.of(getHardshipReviewProgress()));
        return hardship;
    }

    public static HardshipReviewDetail getHardshipReviewDetail() {
        return HardshipReviewDetail.builder()
                .id(4253)
                .detailType(HardshipReviewDetailType.EXPENDITURE)
                .userCreated("test-s")
                .frequency(Frequency.MONTHLY)
                .description("Pension")
                .amount(BigDecimal.valueOf(107.84))
                .accepted("Y")
                .reasonResponse("evidence provided")
                .active(false)
                .build();
    }

    public static HardshipReviewProgress getHardshipReviewProgress() {
        return HardshipReviewProgress.builder()
                .id(1254)
                .userCreated("test-s")
                .userModified("test-s")
                .progressAction(HardshipReviewProgressAction.ADDITIONAL_EVIDENCE)
                .progressResponse(HardshipReviewProgressResponse.FURTHER_RECEIVED)
                .build();
    }

    public static ChildWeightings getChildWeightings() {
        return ChildWeightings.builder()
                .childWeightingId(12)
                .noOfChildren(1)
                .build();
    }

    public static FinancialAssessmentsHistoryDTO getFinancialAssessmentsHistoryDTO() {
        return FinancialAssessmentsHistoryDTO.builder()
                .repId(1234)
                .initialAscrId(1)
                .assessmentType("INIT")
                .newWorkReason(getNewWorkReason())
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated("test-f")
                .cmuId(30)
                .fassInitStatus("COMPLETE")
                .initialAssessmentDate(LocalDateTime.parse("2021-10-09T15:02:25"))
                .initResult("FULL")
                .initApplicationEmploymentStatus("NONPASS")
                .childWeightings(List.of(TestModelDataBuilder.getChildWeightHistoryDTO()))
                .assessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetailsHistoryDTO()))
                .build();
    }

    public static FinancialAssessmentDetailsHistoryDTO getFinancialAssessmentDetailsHistoryDTO() {
        return FinancialAssessmentDetailsHistoryDTO.builder()
                .criteriaDetailId(40)
                .applicantAmount(BigDecimal.valueOf(1650.00))
                .applicantFrequency(Frequency.MONTHLY)
                .partnerAmount(BigDecimal.valueOf(1650.00))
                .partnerFrequency(Frequency.TWO_WEEKLY)
                .fasdId(4321)
                .build();
    }

    public static ChildWeightHistoryDTO getChildWeightHistoryDTO() {
        return ChildWeightHistoryDTO.builder()
                .childWeightingId(2)
                .noOfChildren(1)
                .build();
    }

    public static UpdateAppDateCompleted getUpdateAppDateCompleted() {
        return UpdateAppDateCompleted.builder()
                .repId(REP_ID)
                .assessmentDateCompleted(LocalDateTime.now())
                .build();
    }

    public static String getUpdateAppDateCompletedJson() {
        return "{\n" +
                " \"repId\": " + REP_ID + " ,\n" +
                "  \"assessmentDateCompleted\":\"" + APP_DATE_COMPLETED + "\"\n" +
                "}";
    }

    public static UpdateRepOrder getUpdateRepOrder() {
        return UpdateRepOrder.builder()
                .repId(REP_ID)
                .sentenceOrderDate(LocalDateTime.parse(APP_DATE_COMPLETED).toLocalDate())
                .userModified(TEST_USER)
                .caseId(String.valueOf(TEST_CASE_ID))
                .catyCaseType(String.valueOf(CrownCourtCaseType.APPEAL_CC))
                .appealTypeCode("ACN")
                .arrestSummonsNo(TEST_ARREST_SUMMONS_NUMBER)
                .magsOutcome("COMMITTED FOR TRIAL")
                .magsOutcomeDate(String.valueOf(LocalDate.now().minusDays(10)))
                .magsOutcomeDateSet(LocalDateTime.now().minusDays(10))
                .committalDate(LocalDate.now().minusDays(5))
                .decisionReasonCode("GRANTED")
                .crownRepId(REP_ID)
                .crownRepOrderDecision("Granted - Passed Means Test")
                .crownRepOrderType("Crown Court Only")
                .crownRepOrderDate(LocalDate.now().minusDays(5))
                .crownWithdrawalDate(LocalDate.now().minusDays(3))
                .isImprisoned(true)
                .assessmentDateCompleted(LocalDateTime.now().minusDays(15).toLocalDate())
                .applicantHistoryId(8954)
                .evidenceFeeLevel(null)
                .bankAccountNo(14536598)
                .bankAccountName("John Doe")
                .paymentMethod("STANDING ORDER")
                .preferredPaymentDay(1)
                .sortCode("00-01-02")
                .isSendToCCLF(false)
                .areaId(16)
                .cmuId(245)
                .isCaseTransferred(false)
                .isBenchWarrantIssued(false)
                .appealSentenceOrderChangedDate(null)
                .appealSentenceOrderDate(null)
                .appealReceivedDate(null)
                .appealTypeDate(null)
                .firstCapitalReminderDate(null)
                .allCapitalEvidenceReceivedDate(null)
                .applicationId(2343245)
                .capitalAllowanceReinstatedDate(null)
                .capitalAllowanceWithheldDate(null)
                .capitalEvidenceDueDate(null)
                .capitalNote("Test capital note")
                .capitalAllowance(0)
                .isCourtCustody(false)
                .dateReceived(null)
                .dateStatusDue(null)
                .dateStatusSet(null)
                .decisionDate(null)
                .iojResultNote("Test IOJ Result Note")
                .macoCourt("220")
                .magsWithdrawalDate(null)
                .isNoCapitalDeclared(true)
                .oftyOffenceType("LESSER VIOL/DRUGS")
                .useSuppAddressForPost(true)
                .postalAddressId(38908834)
                .rorsStatus("CURR")
                .statusReason("Test Status Reason")
                .suppAccountCode("0U224X")
                .isWelshCorrespondence(true)
                .cinrCode("NOCON")
                .isPartner(true)
                .isRetrial(true)
                .efmDateStamp(null)
                .solicitorName("Jay Shah")
                .hearingDate(null)
                .build();
    }

    public static String getUpdateRepOrderJson() {
        return "{\n" +
                " \"repId\": " + REP_ID + " ,\n" +
                "\"sentenceOrderDate\": \"" + APP_DATE_COMPLETED + "\",\n" +
                "  \"userModified\": \"" + TEST_USER + "\"\n" +
                "}";
    }

    public static OffenceDTO getOffenceDTO(Integer offenceTxId) {

        return OffenceDTO.builder()
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

    public static WQLinkRegisterDTO getWQLinkRegisterDTO(Integer createdTxId) {

        return WQLinkRegisterDTO.builder()
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

    public static WQHearingDTO getWQHearingDTO(Integer createdTxId) {
        return WQHearingDTO.builder()
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

    public static UpdateCCOutcome getUpdateCCOutcome() {
        return UpdateCCOutcome.builder()
                .repId(REP_ID)
                .ccOutcome("TEST-OUTCOME")
                .benchWarrantIssued("N")
                .appealType("TEST-APPEAL-TYPE")
                .imprisoned("N")
                .caseNumber("123456")
                .crownCourtCode("30")
                .build();
    }

    public static UpdateSentenceOrder getUpdateSentenceOrder() {
        return UpdateSentenceOrder.builder()
                .repId(REP_ID)
                .dbUser("TEST_USER")
                .sentenceOrderDate(LocalDate.now())
                .dateChanged(LocalDate.now())
                .build();
    }

    public static RepOrderCCOutcome getRepOrderCCOutcome() {
        return RepOrderCCOutcome.builder()
                .repId(REP_ID)
                .outcome("CONVICTED")
                .crownCourtCode("430")
                .userCreated(TEST_USER)
                .outcomeDate(TEST_DATE)
                .id(1)
                .build();
    }

    public static RepOrderCCOutcome getUpdateRepOrderCCOutcome(Integer ccOutComeId) {
        return RepOrderCCOutcome.builder()
                .repId(REP_ID)
                .caseNumber(TEST_CASE_ID.toString())
                .outcome("CONVICTED")
                .crownCourtCode("430")
                .id(ccOutComeId)
                .userModified(TEST_USER)
                .build();
    }

    public static RepOrderCCOutcomeDTO getRepOrderCCOutcomeDTO(Integer outcomeId) {
        return RepOrderCCOutcomeDTO.builder()
                .repId(REP_ID)
                .caseNumber(TEST_CASE_ID.toString())
                .outcome("PART CONVICTED")
                .crownCourtCode("459")
                .userCreated(TEST_USER)
                .id(outcomeId)
                .build();

    }

    public static CreateContributions getCreateContributions() {
        return CreateContributions.builder()
                .repId(REP_ID)
                .applId(REP_ID)
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
                .build();
    }

    public static UpdateContributions getUpdateContributions(Integer contributionId) {
        return UpdateContributions.builder()
                .id(contributionId)
                .userModified(USER_NAME)
                .contributionFileId(2)
                .effectiveDate(TEST_DATE.toLocalDate())
                .calcDate(TEST_DATE.toLocalDate())
                .contributionCap(new BigDecimal("8.00"))
                .monthlyContributions(new BigDecimal("8888.00"))
                .upfrontContributions(new BigDecimal("87.00"))
                .upliftApplied("Y")
                .basedOn("Means")
                .transferStatus("RECEIVED")
                .dateUpliftApplied(TEST_DATE.toLocalDate())
                .dateUpliftRemoved(TEST_DATE.toLocalDate())
                .createContributionOrder("Y")
                .correspondenceId(10)
                .ccOutcomeCount(10)
                .seHistoryId(10)
                .build();
    }

    public static ContributionsDTO getContributionsDTO() {
        return ContributionsDTO.builder()
                .repId(REP_ID)
                .applId(REP_ID)
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
                .id(1)
                .build();
    }

    public static ContributionCalcParametersDTO getContributionCalcParametersDTO() {
        return ContributionCalcParametersDTO.builder()
                .fromDate(LocalDateTime.now())
                .disposableIncomePercent(BigDecimal.TEN)
                .upliftedIncomePercent(BigDecimal.ONE)
                .totalMonths(6)
                .upfrontTotalMonths(5)
                .firstReminderDaysDue(28)
                .secondReminderDaysDue(7)
                .build();
    }

    public CourtDataDTO getSaveAndLinkModelRaw() {
        return CourtDataDTO.builder()

                .caseDetails(getCaseDetails())
                .defendantMAATDataEntity(testEntityDataBuilder.getDefendantMAATDataEntity())
                .solicitorMAATDataEntity(testEntityDataBuilder.getSolicitorMAATDataEntity())
                .build();
    }

    public CourtDataDTO getCourtDataDTO() {
        return CourtDataDTO.builder()
                .caseId(123456)
                .libraId("CP25467")
                .proceedingId(12123231)
                .txId(123456)
                .caseDetails(getCaseDetails())
                .defendantMAATDataEntity(testEntityDataBuilder.getDefendantMAATDataEntity())
                .solicitorMAATDataEntity(testEntityDataBuilder.getSolicitorMAATDataEntity())
                .build();
    }

    public CaseDetails getCaseDetails() {
        String jsonString = getSaveAndLinkString();
        return gson.fromJson(jsonString, CaseDetails.class);
    }

    public String getSaveAndLinkString() {
        return "{\n" +
                "  \"maatId\": 1234,\n" +
                "  \"category\": 12,\n" +
                "  \"laaTransactionId\":\"e439dfc8-664e-4c8e-a999-d756dcf112c2\",\n" +
                "  \"caseUrn\":\"caseurn1\",\n" +
                "  \"asn\": \"123456754\",\n" +
                "  \"docLanguage\": \"EN\",\n" +
                "  \"caseCreationDate\": \"2019-08-16\",\n" +
                "  \"cjsAreaCode\": \"16\",\n" +
                "  \"createdUser\": \"testUser\",\n" +
                "  \"cjsLocation\": \"B16BG\",\n" +
                "  \"isActive\" : true,\n" +
                "  \"defendant\": {\n" +
                "    \"defendantId\" : \"Dummy Def ID\",\n" +
                "    \"forename\": \"Test FName\",\n" +
                "    \"surname\": \"Test LName\",\n" +
                "    \"organization\": null,\n" +
                "    \"dateOfBirth\": \"1980-08-16\",\n" +
                "    \"address_line1\": null,\n" +
                "    \"address_line2\": null,\n" +
                "    \"address_line3\": null,\n" +
                "    \"address_line4\": null,\n" +
                "    \"address_line5\": null,\n" +
                "    \"postcode\": \"UB83HW\",\n" +
                "    \"nino\": \"ABCNINUM\",\n" +
                "    \"telephoneHome\": null,\n" +
                "    \"telephoneWork\": null,\n" +
                "    \"telephoneMobile\": null,\n" +
                "    \"email1\": null,\n" +
                "    \"email2\": null,\n" +
                "    \"offences\": [\n" +
                "      {\n" +
                "        \"offenceCode\": \"OffenceCode\",\n" +
                "        \"asnSeq\": \"001\",\n" +
                "        \"offenceShortTitle\": null,\n" +
                "        \"offenceClassification\": null,\n" +
                "        \"offenceDate\": null,\n" +
                "        \"offenceWording\": null,\n" +
                "        \"modeOfTrail\": null,\n" +
                "        \"legalAidStatus\": null,\n" +
                "        \"legalAidStatusDate\": null,\n" +
                "        \"legalAidReason\": null,\n" +
                "        \"results\": [\n" +
                "          {\n" +
                "            \"resultCode\": 3026,\n" +
                "            \"asnSeq\" : \"001\",\n" +
                "            \"resultShortTitle\": null,\n" +
                "            \"resultText\": null,\n" +
                "            \"resultCodeQualifiers\": null,\n" +
                "            \"nextHearingDate\": null,\n" +
                "            \"nextHearingLocation\": null,\n" +
                "            \"firstName\": null,\n" +
                "            \"contactName\": null,\n" +
                "            \"laaOfficeAccount\": null,\n" +
                "            \"legalAidWithdrawalDate\": null,\n" +
                "            \"dateOfHearing\": null,\n" +
                "            \"courtLocation\": null,\n" +
                "            \"sessionValidateDate\": null\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"sessions\": [\n" +
                "    {\n" +
                "      \"courtLocation\": \"B16BG\",\n" +
                "      \"dateOfHearing\": \"2020-08-16\",\n" +
                "      \"postHearingCustody\" :  \"R\",\n" +
                "      \"sessionvalidateddate\": null\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    public String getUnLinkString() {
        return "{\n" +
                " \"maatId\": 1234,\n" +
                "  \"laaTransactionId\":\"e439dfc8-664e-4c8e-a999-d756dcf112c2\",\n" +
                "  \"userId\": \"testUser\",\n" +
                "  \"reasonId\": 1,\n" +
                "  \"otherReasonText\" : \"\"\n" +
                "}";
    }

    public String getUnLinkWithOtherReasonString() {
        return "{\n" +
                " \"maatId\": 1234,\n" +
                "  \"laaTransactionId\":\"e439dfc8-664e-4c8e-a999-d756dcf112c2\",\n" +
                "  \"userId\": \"testUser\",\n" +
                "  \"reasonId\": 1,\n" +
                "  \"otherReasonText\" : \"Other reason description\"\n" +
                "}";
    }

    public HearingDTO getHearingDTO() {
        return HearingDTO.builder()
                .hearingId(HEARING_ID)
                .maatId(REP_ID)
                .jurisdictionType(JURISDICTION_TYPE_MAGISTRATES)
                .caseId(1234)
                .cjsAreaCode("5")
                .proceedingId(9999)
                .txId(123456)
                .caseUrn("caseurn")
                .docLanguage("en")
                .defendant(DefendantDTO.builder().surname("Smith").postcode("LU3 111").build())
                .offence(HearingOffenceDTO.builder().legalAidStatus("AP").asnSeq("0").asnSeq("1").legalAidReason("some aid reason").build())
                .result(getResultDTO())
                .session(getSessionDTO())
                .build();
    }

    public SessionDTO getSessionDTO() {
        return SessionDTO.builder()
                .dateOfHearing("2020-08-16")
                .courtLocation(COURT_LOCATION)
                .sessionValidatedDate("2020-08-16")
                .build();
    }

    public ResultDTO getResultDTO() {
        return ResultDTO.builder()
                .resultCode(6666)
                .resultText("This is a some result text for hearing")
                .nextHearingLocation("London")
                .firmName("Bristol Law Service")
                .resultShortTitle("Next call")
                .build();
    }

    public HearingDTO getHearingDTOForCCOutcome() {

        return HearingDTO
                .builder()
                .maatId(789034)
                .prosecutionConcluded(true)
                .offence(HearingOffenceDTO
                        .builder()
                        .plea(PleaDTO
                                .builder()
                                .offenceId("123456")
                                .pleaValue("NOT_GUILTY")
                                .pleaDate("2020-10-12")
                                .build()
                        )
                        .verdict(VerdictDTO
                                .builder()
                                .verdictCode("CD234")
                                .verdictDate("2020-10-21")
                                .category("Verdict_Category")
                                .categoryType("GUILTY_CONVICTED")
                                .cjsVerdictCode("88999")
                                .build()
                        )
                        .build()
                )

                .build();
    }

    public static CorrespondenceStateDTO buildCorrespondenceStateDTO(Integer repId, String status) {
        return CorrespondenceStateDTO.builder()
                .repId(repId)
                .status(status)
                .build();
    }

    public static CorrespondenceStateEntity buildCorrespondenceStateEntity(Integer repId, String status) {
        return CorrespondenceStateEntity.builder()
                .repId(repId)
                .status(status)
                .build();
    }
}