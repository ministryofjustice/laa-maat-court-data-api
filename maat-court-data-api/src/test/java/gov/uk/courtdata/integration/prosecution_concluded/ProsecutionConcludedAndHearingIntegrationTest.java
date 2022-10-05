package gov.uk.courtdata.integration.prosecution_concluded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.integration.MockCdaWebConfig;
import gov.uk.courtdata.model.Metadata;
import gov.uk.courtdata.prosecutionconcluded.model.*;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;


import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {
                MAATCourtDataApplication.class,
                MockCdaWebConfig.class,
        })
public class ProsecutionConcludedAndHearingIntegrationTest extends MockMvcIntegrationTest {

    private final Integer TEST_MAAT_ID = 6039349;
    private final UUID TEST_HEARING_ID = UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1");
    private final String LAA_TRANSACTION_ID = "61600a90-89e2-4717-aa9b-a01fc66130c1";

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private ProsecutionConcludedListener prosecutionConcludedListener;
    @Autowired
    private WQHearingRepository wqHearingRepository;

    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Autowired
    private ProsecutionConcludedRepository prosecutionConcludedRepository;

    private MockWebServer mockCdaWebServer;
    private QueueMessageLogTestHelper queueMessageLogTestHelper;
    private WQHearingEntity existingWqHearingEntity;

    @BeforeEach
    public void setUp() throws Exception {
        wqHearingRepository.deleteAll();
        queueMessageLogRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        prosecutionConcludedRepository.deleteAll();
        loadData();
        setupCdaWebServer();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
    }

    private void setupCdaWebServer() throws IOException {
        mockCdaWebServer = new MockWebServer();
        mockCdaWebServer.start(1234);
        mockCdaWebServer.enqueue(new MockResponse().setResponseCode(OK.code()));
    }

    private void loadData() {
        existingWqHearingEntity = wqHearingRepository
                .save(WQHearingEntity
                        .builder()
                        .txId(23232)
                        .hearingUUID(TEST_HEARING_ID.toString())
                        .wqJurisdictionType(JurisdictionType.CROWN.name())
                        .maatId(TEST_MAAT_ID)
                        .caseUrn("3CDSRE33")
                        .ouCourtLocation("3434")
                        .build()
                );

        wqLinkRegisterRepository
                .save(WqLinkRegisterEntity
                        .builder()
                        .createdTxId(343431)
                        .maatId(TEST_MAAT_ID)
                        .caseId(12129)
                        .caseUrn("CASEURN233")
                        .build()
                );
    }


    @AfterEach
    public void tearDown() throws IOException {
        mockCdaWebServer.shutdown();
    }

    @Test
    public void givenSqsPayload_whenDataIsValid_thenSendRequestToCda() throws Exception {

        //given
        ProsecutionConcluded prosecutionConcluded = getTestProsecutionConcludedObject();
        prosecutionConcluded.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        prosecutionConcluded.setMaatId(56456);
        String sqsPayload = pullMessageFromSQS(prosecutionConcluded);

        //when
        prosecutionConcludedListener.receive(sqsPayload);

        //then
        queueMessageLogTestHelper.assertQueueMessageLogged(
                sqsPayload, 1, prosecutionConcluded.getMetadata().getLaaTransactionId(), prosecutionConcluded.getMaatId());

        //checked if you get request called?
        RecordedRequest receivedRequest = mockCdaWebServer.takeRequest();

        SoftAssertions.assertSoftly(softly -> {
            assertThat(mockCdaWebServer.getRequestCount())
                    .isEqualTo(1);
            assertThat(Objects.requireNonNull(receivedRequest.getRequestUrl()).toString())
                    .isEqualTo("http://localhost:1234/hearing_results/61600a90-89e2-4717-aa9b-a01fc66130c1?publish_to_queue=true");
            assertThat(LAA_TRANSACTION_ID).isEqualTo(receivedRequest.getHeaders().get("X-Request-ID"));
            assertThat(receivedRequest.getBody().readUtf8()).isEmpty();
        });

        assertThat(prosecutionConcludedRepository.findAll().size()).isEqualTo(1);
        assertThat(prosecutionConcludedRepository.findAll().get(0).getMaatId()).isEqualTo(56456);
    }

    private ProsecutionConcluded getTestProsecutionConcludedObject() {
        return ProsecutionConcluded.builder()
                .prosecutionCaseId(UUID.fromString("998984a0-ae53-466c-9c13-e0c84c1fd581"))
                .defendantId(UUID.fromString("aa07e234-7e80-4be1-a076-5ab8a8f49df5"))
                .isConcluded(true)
                .hearingIdWhereChangeOccurred(UUID.fromString("998984a0-ae53-466c-9c13-e0c84c1fd581"))
                .offenceSummary(List.of(
                        OffenceSummary.builder()
                                .offenceId(UUID.fromString("ed0e9d59-cc1c-4869-8fcd-464caf770744"))
                                .offenceCode("PT00011")
                                .proceedingsConcluded(true)
                                .proceedingsConcludedChangedDate("2022-02-01")
                                .plea(Plea.builder()
                                        .originatingHearingId(UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1"))
                                        .value("GUILTY")
                                        .pleaDate("2022-02-01").build())
                                .verdict(Verdict.builder()
                                        .verdictDate("2022-02-01")
                                        .originatingHearingId(UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1"))
                                        .verdictType(
                                                VerdictType.builder()
                                                        .description("GUILTY")
                                                        .category("GUILTY")
                                                        .categoryType("GUILTY")
                                                        .sequence(4126)
                                                        .verdictTypeId(null)
                                                        .build())
                                        .build())
                                .build()
                ))
                .maatId(6039349)
                .metadata(
                        Metadata.builder().laaTransactionId("61600a90-89e2-4717-aa9b-a01fc66130c1").build())
                .build();
    }

    private String pullMessageFromSQS(ProsecutionConcluded prosecutionConcludedObject) throws JsonProcessingException {
        String messageString = objectMapper.writeValueAsString(prosecutionConcludedObject);
        return messageString.replace("concluded", "isConcluded");
    }
}