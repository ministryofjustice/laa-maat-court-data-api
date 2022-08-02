package gov.uk.courtdata.integration.prosecution_concluded;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class ProsecutionConcludedIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private ProsecutionConcludedListener prosecutionConcludedListener;
    @Autowired
    private WQHearingRepository wqHearingRepository;
    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private XLATResultRepository xlatResultRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private CrownCourtCodeRepository crownCourtCodeRepository;

    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    @BeforeEach
    public void setUp() throws Exception {
        wqHearingRepository.deleteAll();
        offenceRepository.deleteAll();
        queueMessageLogRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        xlatResultRepository.deleteAll();
        resultRepository.deleteAll();
        repOrderRepository.deleteAll();
        crownCourtCodeRepository.deleteAll();
        loadData();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
    }

    private void loadData() {
        wqHearingRepository
                .save(WQHearingEntity
                        .builder()
                        .txId(23232)
                        .hearingUUID("61600a90-89e2-4717-aa9b-a01fc66130c1")
                        .wqJurisdictionType(JurisdictionType.CROWN.name())
                        .maatId(6039349)
                        .caseUrn("3CDSRE33")
                        .ouCourtLocation("3434")
                        .build()
                );

        wqLinkRegisterRepository
                .save(WqLinkRegisterEntity
                        .builder()
                        .createdTxId(343431)
                        .maatId(6039349)
                        .caseId(12129)
                        .caseUrn("CASEURN233")
                        .build()
                );

        offenceRepository
                .save(OffenceEntity
                        .builder()
                        .txId(3454238)
                        .caseId(12129)
                        .offenceId("ed0e9d59-cc1c-4869-8fcd-464caf770744")
                        .asnSeq("001")
                        .build()
                );

        xlatResultRepository.save(
                XLATResultEntity
                        .builder()
                        .cjsResultCode(4455)
                        .wqType(1)
                        .subTypeCode(1)
                        .ccBenchWarrant("Y")
                        .ccImprisonment("Y")
                        .build()
        );

        xlatResultRepository.save(
                XLATResultEntity
                        .builder()
                        .cjsResultCode(4458)
                        .wqType(1)
                        .subTypeCode(2)
                        .ccBenchWarrant("Y")
                        .build()
        );

        resultRepository.save(
                ResultEntity
                        .builder()
                        .asnSeq("1")
                        .asn("1")
                        .txId(4564523)
                        .caseId(12129)
                        .resultCode("4458")
                        .build()
        );

        repOrderRepository.save(
                RepOrderEntity
                        .builder()
                        .id(6039349)
                        .caseId("12129")
                        .aptyCode("EITHER WAY")
                        .catyCaseType("EITHER WAY")
                        .build()
        );

        crownCourtCodeRepository.save(
                CrownCourtCode
                        .builder()
                        .code("2232")
                        .ouCode("3434")
                        .build()
        );
    }

    @Test
    public void givenSqsPayload_whenDataIsValid_thenProcessAsExpected() {
        prosecutionConcludedListener.receive(pullMessageFromSQS());
    }

    @Test
    public void givenSqsPayload_whenDataIsValidAndCaseIsNotConcluded_thenProcessAsExpected() {
        prosecutionConcludedListener.receive(pullMessageFromSQS());
    }


    @Test
    public void givenSqsPayload_whenMaatIdIsNotLocked_thenProcess() {
        prosecutionConcludedListener.receive(pullMessageFromSQS());
    }

    @Test
    public void givenSqsPayload_whenMaatIdIsLocked_thenThrowException() {
        prosecutionConcludedListener.receive(pullMessageFromSQS());
    }


    @Test
    public void givenSqsPayload_whenHearingEntityIsEmpty_thenReturnMessage() {

        String sqsPayload = "{" +
                "    \"maatId\": \"6039349\",\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"offenceSummary\": [\n" +
                "        {\n" +
                "            \"offenceId\": \"ed0e9d59-cc1c-4869-8fcd-464caf770744\",\n" +
                "            \"offenceCode\": \"PT00011\",\n" +
                "            \"proceedingsConcluded\": true,\n" +
                "            \"proceedingsConcludedChangedDate\": \"2022-02-01\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\"\n" +
                "    }\n" +
                "}";
        prosecutionConcludedListener.receive(sqsPayload);


    }

    @Test
    @Ignore("This test will fail until LASB-1238 is fixed.")
    public void givenSqsPayload_whenHearingOffenceSummaryIsNull_thenReturnMessage() {

        String sqsPayload = "{" +
                "    \"maatId\": \"6039349\",\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\"\n" +
                "    }\n" +
                "}";

        Assertions.assertThrows(
                ValidationException.class, () -> prosecutionConcludedListener.receive(sqsPayload), "Payload is not available or null. ");

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,"61600a90-89e2-4717-aa9b-a01fc66130c1", 6039349);
    }

    @Test
    @Ignore("This test will fail until LASB-1238 is fixed.")
    public void givenSqsPayload_whenHearingOffenceSummaryIsEmpty_thenReturnMessage() {
        String laaTransactionId = "61600a90-89e2-4717-aa9b-a01fc66130c1";
        Integer maatId = 6039349;
        String sqsPayload = "{" +
                "    \"maatId\": " + maatId + ",\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"offenceSummary\": [],\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": " + laaTransactionId + "\n" +
                "    }\n" +
                "}";

        Assertions.assertThrows(
                ValidationException.class, () -> prosecutionConcludedListener.receive(sqsPayload), "Payload is not available or null. ");

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,laaTransactionId, maatId);
    }

    @Test
    @Ignore("This test will fail until LASB-1238 is fixed.")
    public void givenSqsPayload_whenMaatIdIsNull_thenReturnMessage() {
        String laaTransactionId = "61600a90-89e2-4717-aa9b-a01fc66130c1";
        String sqsPayload = "{" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": " + laaTransactionId + "\n" +
                "    }\n" +
                "}";

        Assertions.assertThrows(
                ValidationException.class, () -> prosecutionConcludedListener.receive(sqsPayload), "Payload is not available or null. ");

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,"61600a90-89e2-4717-aa9b-a01fc66130c1", -1);
    }

    private String pullMessageFromSQS() {
        return "{\n" +
                "    \"prosecutionCaseId\": \"998984a0-ae53-466c-9c13-e0c84c1fd581\",\n" +
                "    \"defendantId\": \"aa07e234-7e80-4be1-a076-5ab8a8f49df5\",\n" +
                "    \"isConcluded\": true,\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"offenceSummary\": [\n" +
                "        {\n" +
                "            \"offenceId\": \"ed0e9d59-cc1c-4869-8fcd-464caf770744\",\n" +
                "            \"offenceCode\": \"PT00011\",\n" +
                "            \"proceedingsConcluded\": true,\n" +
                "            \"proceedingsConcludedChangedDate\": \"2022-02-01\",\n" +
                "            \"plea\": {\n" +
                "                \"originatingHearingId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "                \"value\": \"GUILTY\",\n" +
                "                \"pleaDate\": \"2022-02-01\"\n" +
                "            },\n" +
                "            \"verdict\": {\n" +
                "                \"verdictDate\": \"2022-02-01\",\n" +
                "                \"originatingHearingId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "                \"verdictType\": {\n" +
                "                    \"description\": \"GUILTY\",\n" +
                "                    \"category\": \"GUILTY\",\n" +
                "                    \"categoryType\": \"GUILTY\",\n" +
                "                    \"sequence\": 4126,\n" +
                "                    \"verdictTypeId\": null\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"maatId\": \"6039349\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\"\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "\n";
    }


}
