package gov.uk.courtdata.builder;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.assessment.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestModelDataBuilder {

    public static final int MAAT_ID = 9988;
    public static final UUID HEARING_ID = UUID.randomUUID();
    public static final JurisdictionType JURISDICTION_TYPE_MAGISTRATES = JurisdictionType.MAGISTRATES;
    public static final String COURT_LOCATION = "London";

    TestEntityDataBuilder testEntityDataBuilder;
    Gson gson;

    public TestModelDataBuilder(TestEntityDataBuilder testEntityDataBuilder, Gson gson) {
        this.gson = gson;
        this.testEntityDataBuilder = testEntityDataBuilder;
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
                .maatId(MAAT_ID)
                .jurisdictionType(JURISDICTION_TYPE_MAGISTRATES)
                .caseId(1234)
                .cjsAreaCode("5")
                .proceedingId(9999)
                .txId(123456)
                .caseUrn("caseurn")
                .docLanguage("en")
                .defendant(DefendantDTO.builder().surname("Smith").postcode("LU3 111").build())
                .offence(OffenceDTO.builder().legalAidStatus("AP").asnSeq("0").asnSeq("1").legalAidReason("some aid reason").build())
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
                .offence(OffenceDTO
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

    public static FinancialAssessmentDTO getFinancialAssessmentDTO() {
        return FinancialAssessmentDTO.builder()
                .repId(5678)
                .initialAscrId(1)
                .assessmentType("INIT")
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
                .build();
    }

    public static FinancialAssessmentDTO getFinancialAssessmentWithDetails() {
        FinancialAssessmentDTO base = getFinancialAssessmentDTO();

        base.setAssessmentDetailsList(List.of(
                FinancialAssessmentDetails.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build(),
                FinancialAssessmentDetails.builder()
                        .criteriaDetailId(45)
                        .applicantAmount(BigDecimal.valueOf(150.00))
                        .applicantFrequency(Frequency.WEEKLY)
                        .build()
        ));
        return base;
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
                .replaced("N")
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
                .replaced("N")
                .valid("Y")
                .dateCompleted(LocalDateTime.parse("2021-10-09T15:01:25"))
                .whoDWPChecked("ABC")
                .build();
    }
}