package gov.uk.courtdata.integration.prosecution_concluded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.Metadata;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.Plea;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.model.Verdict;
import gov.uk.courtdata.prosecutionconcluded.model.VerdictType;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import gov.uk.courtdata.repository.QueueMessageLogRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {
                MAATCourtDataApplication.class
        })
public class ProsecutionConcludedAndHearingIntegrationTest extends MockMvcIntegrationTest {

    private static final String LAA_TRANSACTION_ID = "61600a90-89e2-4717-aa9b-a01fc66130c1";
    private final Integer TEST_MAAT_ID = 6039349;
    private final UUID TEST_HEARING_ID = UUID.fromString(LAA_TRANSACTION_ID);

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

    private QueueMessageLogTestHelper queueMessageLogTestHelper;
    private WQHearingEntity existingWqHearingEntity;

    @BeforeEach
    public void setUp() throws Exception {
        new RepositoryUtil().clearUp(wqHearingRepository,
                queueMessageLogRepository,
                wqLinkRegisterRepository,
                prosecutionConcludedRepository);
        loadData();
        setupCdaWebServer();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
    }

    private void setupCdaWebServer() throws IOException {
        stubForOAuth();
        stubFor(WireMock
                .get(urlEqualTo("http://localhost:9999/api/internal/v1/hearing_results/" + LAA_TRANSACTION_ID + "?publish_to_queue=true"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))));
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

    @Test
    public void givenSqsPayload_whenDataIsValid_thenSendRequestToCda() throws Exception {

        //given
        ProsecutionConcluded prosecutionConcluded = getTestProsecutionConcludedObject();
        prosecutionConcluded.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        prosecutionConcluded.setMaatId(56456);
        String sqsPayload = pullMessageFromSQS(prosecutionConcluded);

        //when
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));

        //then
        queueMessageLogTestHelper.assertQueueMessageLogged(
                sqsPayload, 1, prosecutionConcluded.getMetadata().getLaaTransactionId(), prosecutionConcluded.getMaatId());

        //checked if you get request called?
        verify(exactly(1), postRequestedFor(urlEqualTo("/oauth2/token")));
        verify(exactly(1), getRequestedFor(urlEqualTo("/api/internal/v1/hearing_results/" + LAA_TRANSACTION_ID + "?publish_to_queue=true")));

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
                                        .originatingHearingId(UUID.fromString(LAA_TRANSACTION_ID))
                                        .value("GUILTY")
                                        .pleaDate("2022-02-01").build())
                                .verdict(Verdict.builder()
                                        .verdictDate("2022-02-01")
                                        .originatingHearingId(UUID.fromString(LAA_TRANSACTION_ID))
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
                        Metadata.builder().laaTransactionId(LAA_TRANSACTION_ID).build())
                .build();
    }

    private String pullMessageFromSQS(ProsecutionConcluded prosecutionConcludedObject) throws JsonProcessingException {
        String messageString = objectMapper.writeValueAsString(prosecutionConcludedObject);
        return messageString.replace("concluded", "isConcluded");
    }

    private void stubForOAuth() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> token = Map.of(
                "expires_in", 3600,
                "token_type", "Bearer",
                "access_token", UUID.randomUUID()
        );

        wireMock().stubFor(
                WireMock.post("/oauth2/token").willReturn(
                        WireMock.ok()
                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                .withBody(mapper.writeValueAsString(token))
                )
        );
    }
}