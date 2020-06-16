package gov.uk.courtdata.builder;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.hearing.dto.*;
import gov.uk.courtdata.model.CaseDetails;
import org.springframework.stereotype.Component;

@Component
public class TestModelDataBuilder {

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


    public String hearingString () {

        return "{\n" +
                "  \"maatId\": 1234,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  \"jurisdictionType\": \"CROWN\",\n" +
                "  \"asn\": \"MG25A12456\",\n" +
                "  \"cjsAreaCode\": 16,\n" +
                "  \"caseCreationDate\": \"2018-10-25\",\n" +
                "  \"cjsLocation\": \"B16BG\",\n" +
                "  \"docLanguage\": \"EN\",\n" +
                "  \"inActive\": \"N\",\n" +
                "  \"ccOutComeData\":{\n" +
                "  \"ccooOutcome\": \"SUCCESSFUL\",\n" +
                "     \"crownCourtCode\": \"453\",\n" +
                "     \"benchWarrantIssuedYn\": \"Y\",\n" +
                "     \"ccImprisioned\":\"Y\",\n" +
                "    \"appealType\":\"ACN\",\n" +
                "    \"caseEndDate\": \"2020-08-10\"\n" +
                "  },\n" +
                "  \"defendant\": {\n" +
                "    \"forename\": \"Edward\",\n" +
                "    \"surname\": \"Harrison\",\n" +
                "    \"dateOfBirth\": \"2002-01-10\",\n" +
                "    \"address_line1\": \"Flat 4\",\n" +
                "    \"address_line2\": \"17 Oldberry Road\",\n" +
                "    \"address_line3\": \"Edgeware\",\n" +
                "    \"address_line4\": \"Middlesex\",\n" +
                "    \"address_line5\": \"London\",\n" +
                "    \"postcode\": \"HA8 9DA\",\n" +
                "    \"nino\": \"NH195839C\",\n" +
                "    \"telephoneHome\": null,\n" +
                "    \"telephoneWork\": null,\n" +
                "    \"telephoneMobile\": \"07973 824240\",\n" +
                "    \"email1\": \"teddy_harrison@hotmail.com\",\n" +
                "    \"email2\": null,\n" +
                "    \"offences\": [{\n" +
                "      \"offenceCode\": \"CD98072\",\n" +
                "      \"asnSeq\": 001,\n" +
                "      \"offenceShortTitle\": \"Racially / religiously aggravated wounding / grievous bodily harm\",\n" +
                "      \"offenceClassification\": \"Temporary Offence Classification\",\n" +
                "      \"offenceDate\": \"2018-10-21\",\n" +
                "      \"offenceWording\": \"On 21/10/2018 at Euston Train Station group.\",\n" +
                "      \"modeOfTrial\": 2,\n" +
                "      \"legalAidStatus\": \"AP\",\n" +
                "      \"legalAidStatusDate\": null,\n" +
                "      \"legalAidReason\": \"Application Pending\",\n" +
                "      \"results\": [{\n" +
                "        \"resultCode\": \"4600\",\n" +
                "        \"resultShortTitle\": \"Transfer of solicitor\",\n" +
                "        \"resultText\": \"Transfer of solicitor\",\n" +
                "        \"resultCodeQualifiers\": null,\n" +
                "        \"nextHearingDate\": null,\n" +
                "        \"nextHearingLocation\": \"B16BG\",\n" +
                "        \"firmName\": \"MARTIN MURRAY u0026 ASSOCIATES\",\n" +
                "        \"contactName\": null,\n" +
                "        \"laaOfficeAccount\": \"0A935R\",\n" +
                "        \"legalAidWithdrawalDate\": null\n" +
                "      }]\n" +
                "    }]\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"courtLocation\": \"B16BG\",\n" +
                "    \"dateOfHearing\": \"2018-10-24\",\n" +
                "    \"postHearingCustody\": null,\n" +
                "    \"sessionValidateDate\": \"2020-01-01\"\n" +
                "  }\n" +
                "}" ;

    }

    public String megCourtPayload () {

        return "{\n" +
                "  \"maatId\": 1234,\n" +
                "  \"caseUrn\": \"EITHERWAY\",\n" +
                "  \"jurisdictionType\": \"MAGISTRATES\",\n" +
                "  \"asn\": \"MG25A12456\",\n" +
                "  \"cjsAreaCode\": 16,\n" +
                "  \"caseCreationDate\": \"2018-10-25\",\n" +
                "  \"cjsLocation\": \"B16BG\",\n" +
                "  \"docLanguage\": \"EN\",\n" +
                "  \"inActive\": \"N\",\n" +
                "  \"ccOutComeData\":{\n" +
                "  \"ccooOutcome\": \"SUCCESSFUL\",\n" +
                "     \"crownCourtCode\": \"453\",\n" +
                "     \"benchWarrantIssuedYn\": \"Y\",\n" +
                "     \"ccImprisioned\":\"Y\",\n" +
                "    \"appealType\":\"ACN\",\n" +
                "    \"caseEndDate\": \"2020-08-10\"\n" +
                "  },\n" +
                "  \"defendant\": {\n" +
                "    \"forename\": \"Edward\",\n" +
                "    \"surname\": \"Harrison\",\n" +
                "    \"dateOfBirth\": \"2002-01-10\",\n" +
                "    \"address_line1\": \"Flat 4\",\n" +
                "    \"address_line2\": \"17 Oldberry Road\",\n" +
                "    \"address_line3\": \"Edgeware\",\n" +
                "    \"address_line4\": \"Middlesex\",\n" +
                "    \"address_line5\": \"London\",\n" +
                "    \"postcode\": \"HA8 9DA\",\n" +
                "    \"nino\": \"NH195839C\",\n" +
                "    \"telephoneHome\": null,\n" +
                "    \"telephoneWork\": null,\n" +
                "    \"telephoneMobile\": \"07973 824240\",\n" +
                "    \"email1\": \"teddy_harrison@hotmail.com\",\n" +
                "    \"email2\": null,\n" +
                "    \"offences\": [{\n" +
                "      \"offenceCode\": \"CD98072\",\n" +
                "      \"asnSeq\": 001,\n" +
                "      \"offenceShortTitle\": \"Racially / religiously aggravated wounding / grievous bodily harm\",\n" +
                "      \"offenceClassification\": \"Temporary Offence Classification\",\n" +
                "      \"offenceDate\": \"2018-10-21\",\n" +
                "      \"offenceWording\": \"On 21/10/2018 at Euston Train Station group.\",\n" +
                "      \"modeOfTrial\": 2,\n" +
                "      \"legalAidStatus\": \"AP\",\n" +
                "      \"legalAidStatusDate\": null,\n" +
                "      \"legalAidReason\": \"Application Pending\",\n" +
                "      \"results\": [{\n" +
                "        \"resultCode\": \"4600\",\n" +
                "        \"resultShortTitle\": \"Transfer of solicitor\",\n" +
                "        \"resultText\": \"Transfer of solicitor\",\n" +
                "        \"resultCodeQualifiers\": null,\n" +
                "        \"nextHearingDate\": null,\n" +
                "        \"nextHearingLocation\": \"B16BG\",\n" +
                "        \"firmName\": \"MARTIN MURRAY u0026 ASSOCIATES\",\n" +
                "        \"contactName\": null,\n" +
                "        \"laaOfficeAccount\": \"0A935R\",\n" +
                "        \"legalAidWithdrawalDate\": null\n" +
                "      }]\n" +
                "    }]\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"courtLocation\": \"B16BG\",\n" +
                "    \"dateOfHearing\": \"2018-10-24\",\n" +
                "    \"postHearingCustody\": null,\n" +
                "    \"sessionValidateDate\": \"2020-01-01\"\n" +
                "  }\n" +
                "}" ;

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
                "  \"reasonText\" : \"Test Data\"\n" +
                "}";
    }





    public HearingDTO getHearingDTO() {
        return HearingDTO.builder()
                .maatId(9988)
                .caseId(1234)
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

    public SessionDTO getSessionDTO(){
        return SessionDTO.builder()
                .dateOfHearing("2020-08-16")
                .courtLocation("London")
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

}
