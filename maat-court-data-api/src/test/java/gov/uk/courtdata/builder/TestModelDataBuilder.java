package gov.uk.courtdata.builder;

import com.google.gson.Gson;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.dto.*;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.entity.ReservationsEntity;
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
import uk.gov.justice.laa.crime.enums.HardshipReviewStatus;
import uk.gov.justice.laa.crime.enums.HardshipReviewDetailType;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

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
    public static final int MOCK_HRD_ID = 4253;
    public static final Integer CASE_ID = 123;
    public static final Integer CMU_ID = 456;
    public static final Integer AREA_ID = 789;
    public static final String DATE_RECEIVED = "2023-12-09";

    public static final String SEND_TO_CCLF = "y";
    public static final List<String> NEW_WORK_REASON_LIST = List.of("TEST_NWREASON");
    public static final List<String> ROLE_ACTIONS_LIST = List.of("TEST_ROLE");
    public static final ReservationsEntity RESERVATIONS_ENTITY = new ReservationsEntity();
    public static final ReservationsDTO RESERVATIONS_DTO = new ReservationsDTO();
    public static final Integer RESERVATION_ID = 100000;

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

    public static List<RepOrderApplicantLinksDTO> getRepOrderApplicantLinksDTOs(Integer id) {
        return List.of(getRepOrderApplicantLinksDTO(id));
    }

    public static RepOrderApplicantLinksDTO getRepOrderApplicantLinksDTO(Integer id) {
        return RepOrderApplicantLinksDTO.builder()
                .id(id)
                .repId(2522394)
                .partnerAphiId(11553872)
                .partnerApplId(11553844)
                .linkDate(LocalDate.parse("2021-10-09"))
                .unlinkDate(LocalDate.parse("2021-10-22"))
                .userCreated("test-u")
                .userModified("test-x")
                .build();
    }

    public static ApplicantDisabilitiesDTO getApplicantDisabilitiesDTO() {
        return ApplicantDisabilitiesDTO.builder()
                .applId(5136528)
                .userCreated("TEST-S")
                .userModified("TEST-M")
                .disaDisability("NO COMMENT")
                .build();
    }

    public static ApplicantDisabilitiesDTO getApplicantDisabilitiesDTO(Integer id) {
        return ApplicantDisabilitiesDTO.builder()
                .id(id)
                .applId(5136528)
                .userCreated("TEST-S")
                .userModified("TEST-M")
                .disaDisability("NO COMMENT")
                .build();
    }

    public static ApplicantHistoryDTO getApplicantHistoryDTO(Integer id, String sendToCclf) {
        return ApplicantHistoryDTO.builder()
                .id(id)
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
                .userCreated("TEST")
                .build();
    }

    public static FinancialAssessmentDTO getFinancialAssessmentWithChildWeightings() {
        FinancialAssessmentDTO financialAssessment = getFinancialAssessmentDTO();
        financialAssessment.setChildWeightings(List.of(getChildWeightings()));
        return financialAssessment;
    }

    public static FinancialAssessmentDTO getFinancialAssessmentWithIncomeEvidence() {
        FinancialAssessmentDTO financialAssessment = getFinancialAssessmentDTO();
        financialAssessment.setId(100);
        financialAssessment.setFinAssIncomeEvidences(List.of(getFinAssIncomeEvidenceDTO()));
        return financialAssessment;
    }

    public static FinAssIncomeEvidenceDTO getFinAssIncomeEvidenceDTO(){
        return FinAssIncomeEvidenceDTO.builder()
                .dateReceived(LocalDateTime.parse("2021-10-09T15:01:25"))
                .dateCreated(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER)
                .userModified(TEST_USER)
                .active("Y")
                .incomeEvidence("INE")
                .build();
    }


    public static FinancialAssessmentIncomeEvidence getFinancialAssessmentIncomeEvidence() {
        return FinancialAssessmentIncomeEvidence.builder()
                .dateReceived(LocalDateTime.parse("2021-10-09T15:01:25"))
                .userCreated(TEST_USER)
                .userModified(TEST_USER)
                .incomeEvidence("WAGE SLIP")
                .build();
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
        return """
                {
                "repId": 4351623,
                "initialAscrId": 1,
                "nworCode": "FMA",
                "userCreated": "test-f",
                "cmuId": 30,
                "fassInitStatus": "COMPLETE",
                "initialAssessmentDate": "2021-10-09T15:02:25",
                "initOtherBenefitNote": null,
                "initOtherIncomeNote": null,
                "initTotAggregatedIncome": 15600.0,
                "initAdjustedIncomeValue": 15600.0,
                "initNotes": null,
                "initResult": "FULL",
                "initResultReason": null,
                "incomeEvidenceDueDate": null,
                "incomeUpliftRemoveDate": null,
                "incomeUpliftApplyDate": null,
                "incomeEvidenceNotes": null,
                "initApplicationEmploymentStatus": "NONPASS",
                "usn": null,
                "rtCode": null
                }""";
    }

    public static String getUpdateFinancialAssessmentJson() {
        return """
                {
                "id": 1234,
                "repId": 5678,
                "initialAscrId": 1,
                "assessmentType": "FULL",
                "nworCode": "FMA",
                "cmuId": 30,
                "fassInitStatus": "COMPLETE",
                "initialAssessmentDate": "2006-10-09T00:00:00",
                "initOtherBenefitNote": null,
                "initOtherIncomeNote": null,
                "initTotAggregatedIncome": 15600.0,
                "initAdjustedIncomeValue": 15600.0,
                "initNotes": null,
                "initResult": "FULL",
                "initResultReason": null,
                "incomeEvidenceDueDate": null,
                "incomeUpliftRemoveDate": null,
                "incomeUpliftApplyDate": null,
                "incomeEvidenceNotes": null,
                "initApplicationEmploymentStatus": "NONPASS",
                "fassFullStatus": "COMPLETE",
                "fullAssessmentDate": "2006-10-09T00:00:00",
                "fullResultReason": null,
                "fullAssessmentNotes": null,
                "fullResult": "FAIL",
                "fullAdjustedLivingAllowance": 5304.0,
                "fullTotalAnnualDisposableIncome": 15314.0,
                "fullOtherHousingNote": null,
                "fullTotalAggregatedExpenses": null,
                "fullAscrId": 1,
                "dateCompleted": "2006-10-09T00:00:00",
                "userModified": "dohe-f"
                }""";
    }

    public static String getUpdateContributionsJson() {
        return """
                {
                "id": 999,
                "userModified": "test",
                "contributionFileId": 9,
                "effectiveDate": "2023-04-01T00:00:00.00",
                "calcDate": "2023-04-01T00:00:00.00",
                "contributionCap": 9999,
                "monthlyContributions": 99,
                "upfrontContributions": 9,
                "upliftApplied": "Y",
                "basedOn": "Means",
                "transferStatus": "RECEIVED",
                "dateUpliftApplied": "2023-04-01T00:00:00.00",
                "dateUpliftRemoved": "2023-04-02T00:00:00.00",
                "createContributionOrder": "Y",
                "correspondenceId": 9,
                "ccOutcomeCount": 9,
                "seHistoryId": 9
                }""";
    }

    public static String getInvalidUpdateContributionsJson() {
        return """
                {
                "id": 999,
                "userModified": "test",
                "contributionFileId": 9,
                "calcDate": "2023-04-01T00:00:00.00",
                "contributionCap": 9999,
                "monthlyContributions": 99,
                "upfrontContributions": 9,
                "upliftApplied": "Y",
                "basedOn": "Means",
                "transferStatus": "RECEIVED",
                "dateUpliftApplied": "2023-04-01T00:00:00.00",
                "dateUpliftRemoved": "2023-04-02T00:00:00.00",
                "createContributionOrder": "Y",
                "correspondenceId": 9,
                "ccOutcomeCount": 9,
                "seHistoryId": 9
                }""";
    }

    public static String getCreateContributionsJson() {
        return """
                {
                "repId": 999,
                "applId": 999,
                "userCreated": "test",
                "contributionFileId": 9,
                "effectiveDate": "2023-04-01T00:00:00.00",
                "calcDate": "2023-04-01T00:00:00.00",
                "contributionCap": 9999,
                "monthlyContributions": 99,
                "upfrontContributions": 9,
                "upliftApplied": "Y",
                "basedOn": "Means",
                "transferStatus": "RECEIVED",
                "dateUpliftApplied": "2023-04-01T00:00:00.00",
                "dateUpliftRemoved": "2023-04-02T00:00:00.00",
                "createContributionOrder": "Y",
                "correspondenceId": 9,
                "ccOutcomeCount": 9,
                "seHistoryId": 9
                }""";
    }

    public static String getInvalidCreateContributionsJson() {
        return """
                {
                "repId": 999,
                "applId": 999,
                "userCreated": "",
                "contributionFileId": 9,
                "effectiveDate": "2023-04-01T00:00:00.00",
                "calcDate": "2023-04-01T00:00:00.00",
                "contributionCap": 9999,
                "monthlyContributions": 99,
                "upfrontContributions": 9,
                "upliftApplied": "Y",
                "basedOn": "Means",
                "transferStatus": "RECEIVED",
                "dateUpliftApplied": "2023-04-01T00:00:00.00",
                "dateUpliftRemoved": "2023-04-02T00:00:00.00",
                "createContributionOrder": "Y",
                "correspondenceId": 9,
                "ccOutcomeCount": 9,
                "seHistoryId": 9
                }""";
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
        return """
                {
                "financialAssessmentId": "2000",
                "repId": "1234567",
                "nworCode": "FMA",
                "dateCreated": "2021-10-09T15:02:25",
                "userCreated": "test-f",
                "cmuId": 30,
                "assessmentDate": "2021-10-09T15:02:25",
                "partnerBenefitClaimed": "Y",
                "partnerFirstName": "Test",
                "partnerSurname": "Partner",
                "partnerNiNumber": "AB123456C",
                "partnerDob": "1978-10-09T06:00:00",
                "incomeSupport": "Y",
                "jobSeekers": "Y",
                "statePensionCredit": "N",
                "under18FullEducation": "N",
                "under16": "N",
                "pcobConfirmation": "AGEREL",
                "result": "PASS",
                "dateModified": "2021-10-09T15:01:25",
                "userModified": "test-f",
                "dwpResult": "Yes",
                "between16And17": "N",
                "under18HeardInYouthCourt": "N",
                "under18HeardInMagsCourt": "N",
                "lastSignOnDate": "2021-08-09T12:12:48",
                "esa": "N",
                "pastStatus": "COMPLETE",
                "replaced": "N",
                "valid": "Y",
                "dateCompleted": "2021-10-09T15:02:25",
                "usn": "1234",
                "whoDWPChecked": "ABC",
                "rtCode": "DEF"
                }""";
    }

    public static String getUpdatePassportAssessmentJson() {
        return """
                {
                "id": "1000",
                "repId": "1234567",
                "nworCode": "FMA",
                "cmuId": 30,
                "assessmentDate": "2021-10-09T15:02:25",
                "partnerBenefitClaimed": "Y",
                "partnerFirstName": "Test",
                "partnerSurname": "Partner",
                "partnerNiNumber": "AB123456C",
                "partnerDob": "1978-10-09T06:00:00",
                "incomeSupport": "Y",
                "jobSeekers": "Y",
                "statePensionCredit": "N",
                "under18FullEducation": "N",
                "under16": "N",
                "pcobConfirmation": "DWP",
                "result": "PASS",
                "dwpResult": "Yes",
                "between16And17": "N",
                "under18HeardInYouthCourt": "N",
                "under18HeardInMagsCourt": "N",
                "lastSignOnDate": "2021-08-09T12:12:48",
                "esa": "N",
                "pastStatus": "COMPLETE",
                "replaced": "N",
                "valid": "Y",
                "dateCompleted": "2021-10-09T15:02:25",
                "whoDWPChecked": "ABC",
                "userModified": "test-f"
                }""";
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
                                .rate(DataBuilderUtil.createScaledBigDecimal(183.00))
                                .hours(DataBuilderUtil.createScaledBigDecimal(12.00))
                                .vat(DataBuilderUtil.createScaledBigDecimal(384.25))
                                .disbursements(DataBuilderUtil.createScaledBigDecimal(0.00))
                                .estimatedTotal(DataBuilderUtil.createScaledBigDecimal(2580.25))
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
                """
                        {
                        "nworCode":"NEW",
                        "cmuId": 253,
                        "reviewResult": "FAIL",
                        "resultDate": "2022-01-01T10:00:00",
                        "reviewDate": "2022-01-01T10:00:00",
                        "notes": "",
                        "decisionNotes": "",
                        "solicitorCosts": {
                           "solicitorRate": 183.0,
                           "solicitorHours": 12.0,
                           "solicitorVat": 384.25,
                           "solicitorDisb": 0.0,
                           "solicitorEstTotalCost": 2580.25
                        },
                        "disposableIncome": 4215.46,
                        "disposableIncomeAfterHardship": 2921.38,
                        "status": "COMPLETE",
                        "repId": 621580,
                        "userCreated": "test-s",
                        "courtType": "MAGISTRATE",
                        "financialAssessmentId": 349211
                        """;

        if (withRelationships) {
            json = json +
                    ",\"reviewDetails\": [{\n" +
                    "   \"frequency\": \"MONTHLY\",\n" +
                    "   \"amount\": 107.84,\n" +
                    "   \"accepted\": \"Y\",\n" +
                    "   \"type\": \"EXPENDITURE\",\n" +
                    "   \"detailReason\": \"Evidence Supplied\"\n" +
                    "}],\n" +
                    "\"reviewProgressItems\": [{\n" +
                    "   \"progressAction\": \"ADDITIONAL EVIDENCE\",\n" +
                    "   \"progressResponse\": \"FURTHER RECEIVED\"\n" +
                    "}]\n";
        }

        return json + "}";
    }

    public static String getUpdateHardshipReviewJson(boolean withRelationships) {
        String json =
                """
                        {
                        "id": 1000,
                        "userModified": "test-s",
                        "updated": "2022-01-01T10:00:00",
                        "nworCode":"NEW",
                        "cmuId": 253,
                        "reviewResult": "FAIL",
                        "resultDate": "2022-01-01T10:00:00",
                        "reviewDate": "2022-01-01T10:00:00",
                        "notes": "",
                        "decisionNotes": "",
                        "solicitorCosts": {
                           "solicitorRate": 183.0,
                           "solicitorHours": 12.0,
                           "solicitorVat": 384.25,
                           "solicitorDisb": 0.0,
                           "solicitorEstTotalCost": 2580.25
                        },
                        "disposableIncome": 4215.46,
                        "disposableIncomeAfterHardship": 2921.38,
                        "status": "COMPLETE",
                        "repId": 621580
                        """;

        if (withRelationships) {
            json = json +
                    ",\"reviewDetails\": [{\n" +
                    "   \"frequency\": \"MONTHLY\",\n" +
                    "   \"amount\": 107.84,\n" +
                    "   \"accepted\": \"Y\",\n" +
                    "   \"type\": \"EXPENDITURE\",\n" +
                    "   \"detailReason\": \"Evidence Supplied\"\n" +
                    "}],\n" +
                    "\"reviewProgressItems\": [{\n" +
                    "   \"progressAction\": \"ADDITIONAL EVIDENCE\",\n" +
                    "   \"progressResponse\": \"FURTHER RECEIVED\"\n" +
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

    public static RepOrderMvoDTO getRepOrderMvoDTO(Integer id, Integer repId) {
        return RepOrderMvoDTO.builder()
                .id(id)
                .rep(RepOrderDTO.builder()
                             .id(repId)
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
                .id(MOCK_HRD_ID)
                .detailType(HardshipReviewDetailType.EXPENDITURE)
                .userCreated("test-s")
                .frequency(Frequency.MONTHLY)
                .description("Pension")
                .amount(BigDecimal.valueOf(107.84))
                .accepted("Y")
                .reasonResponse("evidence provided")
                .detailReason(HardshipReviewDetailReason.EVIDENCE_SUPPLIED)
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

    public static String getUpdateAppDateCompletedJson(Integer repId) {
        return "{\n" +
                " \"repId\": " + repId + " ,\n" +
                "  \"assessmentDateCompleted\":\"" + APP_DATE_COMPLETED + "\"\n" +
                "}";
    }

    public static String getCreateRepOrderJson() {
        return "{\n" +
                " \"repId\": " + REP_ID + " ,\n" +
                " \"caseId\": " + CASE_ID + " ,\n" +
                " \"cmuId\": " + CMU_ID + " ,\n" +
                " \"areaId\": " + AREA_ID + " ,\n" +
                " \"dateReceived\": \"" + DATE_RECEIVED + "\" ,\n" +
                "  \"userCreated\": \"" + TEST_USER + "\"\n" +
                "}";

    }

    public static CreateRepOrder getCreateRepOrder() {
        return CreateRepOrder.builder()
                .sentenceOrderDate(LocalDateTime.parse(APP_DATE_COMPLETED).toLocalDate())
                .userCreated(TEST_USER)
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
                .dateReceived(LocalDate.now().minusDays(15))
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
                .appSignedDate(null)
                .usn(333333335)
                .build();
    }

    public static UpdateRepOrder getUpdateRepOrder(Integer repId) {
        return UpdateRepOrder.builder()
                .repId(repId)
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

    public static RepOrderCCOutcome getUpdateRepOrderCCOutcome(Integer ccOutComeId, Integer repId) {
        return RepOrderCCOutcome.builder()
                .repId(repId)
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

    public static CreateContributions getCreateContributions(Integer repId) {
        return CreateContributions.builder()
                .repId(repId)
                .applId(repId)
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

    public static ContributionsSummaryView getContributionsSummaryView() {
        return new ContributionsSummaryView() {
            @Override
            public Integer getId() {
                return 999;
            }

            @Override
            public BigDecimal getMonthlyContributions() {
                return BigDecimal.valueOf(50);
            }

            @Override
            public BigDecimal getUpfrontContributions() {
                return BigDecimal.valueOf(250);
            }

            @Override
            public String getBasedOn() {
                return null;
            }

            @Override
            public String getUpliftApplied() {
                return "N";
            }

            @Override
            public LocalDate getEffectiveDate() {
                return LocalDate.now();
            }

            @Override
            public LocalDate getCalcDate() {
                return LocalDate.now();
            }
        };
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

    public static CorrespondenceStateEntity buildCorrespondenceStateEntity(Integer repId, CorrespondenceStatus status) {
        return CorrespondenceStateEntity.builder()
                .repId(repId)
                .status(status.getStatus())
                .build();
    }

    public static UserSummaryDTO getUserSummaryDTO() {
        return UserSummaryDTO.builder()
                .username(TEST_USER)
                .newWorkReasons(NEW_WORK_REASON_LIST)
                .roleActions(ROLE_ACTIONS_LIST)
                .reservationsDTO(RESERVATIONS_DTO)
                .build();
    }

    public static AssessorDetails getAssessorDetails() {
        return AssessorDetails.builder()
                .fullName("Karen Greaves")
                .userName(TestEntityDataBuilder.ASSESSOR_USER_NAME)
                .build();
    }

    public static ReservationsEntity getReservationsEntity() {
        return ReservationsEntity.builder()
                .recordId(RESERVATION_ID)
                .recordName("mock-record")
                .userName("mock-user")
                .userSession("mock-session")
                .build();
    }

    public static Applicant getApplicant(Integer id) {
        return Applicant.builder()
                .id(id)
                .firstName("test-first-name")
                .lastName("test-last-name")
                .dob(LocalDate.now())
                .bankAccountName("test-bank-account-name")
                .bankAccountNo("test-bank-account-no")
                .dateCreated(LocalDateTime.now())
                .userCreated("test-user")
                .build();
    }

    public CourtDataDTO getSaveAndLinkModelRaw() {
        return CourtDataDTO.builder()

                .caseDetails(getCaseDetails(REP_ID))
                .defendantMAATDataEntity(testEntityDataBuilder.getDefendantMAATDataEntity())
                .solicitorMAATDataEntity(testEntityDataBuilder.getSolicitorMAATDataEntity())
                .build();
    }

    public CourtDataDTO getSaveAndLinkModelRaw(Integer repId) {
        return CourtDataDTO.builder()

                .caseDetails(getCaseDetails(repId))
                .defendantMAATDataEntity(testEntityDataBuilder.getDefendantMAATDataEntity(repId))
                .solicitorMAATDataEntity(testEntityDataBuilder.getSolicitorMAATDataEntity(repId))
                .build();
    }

    public CourtDataDTO getCourtDataDTO() {
        return CourtDataDTO.builder()
                .caseId(123456)
                .libraId("CP25467")
                .proceedingId(12123231)
                .txId(123456)
                .caseDetails(getCaseDetails(REP_ID))
                .defendantMAATDataEntity(testEntityDataBuilder.getDefendantMAATDataEntity())
                .solicitorMAATDataEntity(testEntityDataBuilder.getSolicitorMAATDataEntity())
                .build();
    }

    public CaseDetails getCaseDetails(Integer repId) {
        String jsonString = getSaveAndLinkString(repId);
        return gson.fromJson(jsonString, CaseDetails.class);
    }

    public String getSaveAndLinkString(Integer repId) {
        return "{\n" +
                "  \"maatId\": " + repId + ",\n" +
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
        return """
                {
                 "maatId": 1234,
                  "laaTransactionId":"e439dfc8-664e-4c8e-a999-d756dcf112c2",
                  "userId": "testUser",
                  "reasonId": 1,
                  "otherReasonText" : ""
                }""";
    }

    public String getUnLinkString(Integer repId) {
        return "{\n" +
                " \"maatId\":" + repId + ",\n" +
                "  \"laaTransactionId\":\"e439dfc8-664e-4c8e-a999-d756dcf112c2\",\n" +
                "  \"userId\": \"testUser\",\n" +
                "  \"reasonId\": 1,\n" +
                "  \"otherReasonText\" : \"\"\n" +
                "}";
    }

    public String getUnLinkWithOtherReasonString() {
        return """
                {
                 "maatId": 1234,
                  "laaTransactionId":"e439dfc8-664e-4c8e-a999-d756dcf112c2",
                  "userId": "testUser",
                  "reasonId": 1,
                  "otherReasonText" : "Other reason description"
                }""";
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
                .offence(HearingOffenceDTO.builder().legalAidStatus("AP").asnSeq("0").asnSeq("1")
                                 .legalAidReason("some aid reason").build())
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
}