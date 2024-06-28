package gov.uk.courtdata.integration.repOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.repository.RepOrderMvoRegRepository;
import gov.uk.courtdata.repository.RepOrderMvoRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class RepOrderControllerIntegrationTest extends MockMvcIntegrationTest {

    public static final Integer INVALID_REP_ID = 9999;
    public static final Integer INVALID_MVO_ID = 8888;
    public static final String BASE_URL = "/api/internal/v1/assessment/rep-orders";
    public static final String SLASH = "/";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";
    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";
    private static final String FDC_DELAYED_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/eligibleForFdcDelayedPickup";
    private static final String FDC_FAST_TRACK_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/eligibleForFdcFastTracking";
    private static final String CURRENT_REGISTRATION = "current-registration";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    private RepOrderEntity repOrderValid;
    private RepOrderEntity repOrderValid2;
    private RepOrderEntity repOrderFuture;
    private RepOrderEntity repOrderFuture2;
    public Integer REP_ORDER_ID_NO_SENTENCE_ORDER_DATE;
    public Integer REP_ID;


    @Autowired
    private RepOrderMapper mapper;

    @InjectSoftAssertions
    private SoftAssertions softly;


    @BeforeEach
    void setUp(@Autowired RepOrderRepository repOrderRepository,
               @Autowired RepOrderMvoRepository repOrderMvoRepository,
               @Autowired RepOrderMvoRegRepository repOrderMvoRegRepository) {

        RepOrderEntity repOrdTestData = TestEntityDataBuilder.getPopulatedRepOrder();
        repOrdTestData.setSentenceOrderDate(null);
        RepOrderEntity repOrder = repOrderRepository.save(repOrdTestData);
        REP_ORDER_ID_NO_SENTENCE_ORDER_DATE = repOrder.getId();

        RepOrderEntity repOrderEntity = repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder());
        REP_ID = repOrderEntity.getId();

        repOrderMvoRepository.save(
                TestEntityDataBuilder.getRepOrderMvoEntity(TestEntityDataBuilder.MVO_ID, repOrder)
        );
        repOrderMvoRegRepository.save(
                TestEntityDataBuilder.getRepOrderMvoRegEntity(TestEntityDataBuilder.REP_ID, repOrder)
        );
    }

    private RepOrderDTO getUpdatedRepOrderDTO() {
        RepOrderEntity repOrderEntity = repos.repOrder.getReferenceById(REP_ID);
        RepOrderDTO repOrderDTO = TestModelDataBuilder.getRepOrderDTO(REP_ID);
        repOrderDTO.setDateModified(repOrderEntity.getDateModified());
        repOrderDTO.setSentenceOrderDate(repOrderEntity.getSentenceOrderDate());
        return repOrderDTO;
    }

    @Test
    void givenInvalidRepId_whenFindInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order found for ID: " + INVALID_REP_ID,
                get(BASE_URL + SLASH + INVALID_REP_ID)));
    }

    @Test
    void givenIncorrectRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order found for ID: " + REP_ORDER_ID_NO_SENTENCE_ORDER_DATE,
                get(BASE_URL + SLASH + REP_ORDER_ID_NO_SENTENCE_ORDER_DATE + "?has_sentence_order_date=true")
        ));
    }

    @Test
    void givenValidRepId_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        assertTrue(runSuccessScenario(repOrderDTO, get(BASE_URL + SLASH + REP_ID)));
    }

    @Test
    void givenValidRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        assertTrue(runSuccessScenario(repOrderDTO,
                get(BASE_URL + SLASH + REP_ID + "?has_sentence_order_date=true")
        ));
    }

    @Test
    void givenRepIdIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Rep Id is missing from request and is required",
                post(BASE_URL + SLASH + "update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        UpdateAppDateCompleted.builder()
                                                .build()
                                )
                        )
        ));
    }

    @Test
    void givenDateIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Assessment Date completed is missing from request and is required",
                post(BASE_URL + SLASH + "update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        UpdateAppDateCompleted.builder()
                                                .repId(REP_ID)
                                                .build()
                                )
                        )
        ));
    }

    @Test
    void givenInvalidRepId_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("MAAT/REP ID [" + INVALID_REP_ID + "] is invalid",
                post(BASE_URL + SLASH + "update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        UpdateAppDateCompleted.builder()
                                                .repId(INVALID_REP_ID)
                                                .assessmentDateCompleted(LocalDateTime.now())
                                                .build()
                                )
                        )
        ));
    }

    @Test
    void givenValidParameters_whenUpdateAppDateCompletedIsInvoked_theCompletedDateShouldUpdate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter).toLocalDate();

        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + SLASH + "update-date-completed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson(REP_ID))
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repos.repOrder.getReferenceById(REP_ID);
        assertThat(repOrderEntity.getId()).isEqualTo(REP_ID);
        assertThat(repOrderEntity.getAssessmentDateCompleted()).isEqualTo(expectedDate);
    }

    @Test
    void givenInvalidMvoId_whenFindByCurrentRegistrationIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order MVO Reg found for ID: " + INVALID_MVO_ID,
                get(MVO_REG_ENDPOINT_URL + "/" + INVALID_MVO_ID + "/" + CURRENT_REGISTRATION)
        ));
    }

    @Test
    void givenValidMvoId_whenFindByCurrentRegistrationIsInvoked_thenRepOrderMvoRegIsReturned() throws Exception {
        assertTrue(runSuccessScenario(List.of(TestModelDataBuilder.getRepOrderMvoRegDTO()),
                get(MVO_REG_ENDPOINT_URL + "/" + TestEntityDataBuilder.MVO_ID + "/" + CURRENT_REGISTRATION)
        ));
    }

    @Test
    void givenInvalidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order MVO found for ID: " + INVALID_REP_ID,
                get(MVO_ENDPOINT_URL + "/" + INVALID_REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES)
        ));
    }

    @Test
    void givenValidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoIsReturned() throws Exception {
        assertTrue(runSuccessScenario(TestModelDataBuilder.getRepOrderMvoDTO(TestModelDataBuilder.MVO_ID, REP_ORDER_ID_NO_SENTENCE_ORDER_DATE),
                get(MVO_ENDPOINT_URL + "/" + REP_ORDER_ID_NO_SENTENCE_ORDER_DATE + "?owner=" + VEHICLE_OWNER_INDICATOR_YES)
        ));
    }

    @Test
    void givenValidParameters_whenCreateIsInvoked_theRepOrderIsCreated() throws Exception {

        CreateRepOrder request = TestModelDataBuilder.getCreateRepOrder();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.caseId").value(request.getCaseId()))
                .andExpect(jsonPath("$.catyCaseType").value(request.getCatyCaseType()));
    }

    @Test
    void givenRepIdIsMissing_whenUpdateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario(
            "MAAT/REP ID is required, found [null]",
                put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateRepOrder.builder()
                                        .build())
                        )
        ));
    }

    @Test
    void givenInvalidRepId_whenUpdateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario(
            "MAAT/REP ID [" + INVALID_REP_ID + "] is invalid",
                put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateRepOrder.builder()
                                        .repId(INVALID_REP_ID)
                                        .build())
                        )
        ));
    }

    @Test
    void givenValidParameters_whenUpdateIsInvoked_theRepOrderIsUpdated() throws Exception {

        UpdateRepOrder request = TestModelDataBuilder.getUpdateRepOrder(REP_ID);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getRepId()))
                .andExpect(jsonPath("$.userModified").value(request.getUserModified()))
                .andExpect(jsonPath("$.crownRepOrderDecision").value(request.getCrownRepOrderDecision()));
    }

    @Test
    void givenHeadRequestWithRepId_whenFindIsInvoked_thenReturnMetadata() throws Exception {
        var response = runSuccessScenario(
                head(BASE_URL + SLASH + REP_ID)
        );
        var content = response.getResponse().getContentAsString();

        softly.assertThat(content).isEqualTo("");
        softly.assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");

        response = runSuccessScenario(
                head(BASE_URL + SLASH + INVALID_REP_ID)
        );
        content = response.getResponse().getContentAsString();

        softly.assertThat(content).isEqualTo("");
        softly.assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("0");
    }

    @Test
    void givenValidRepId_whenDeleteIsInvoked_theRepOrderIsDeleted() throws Exception {

        RepOrderEntity repOrderEntity = TestEntityDataBuilder.getPopulatedRepOrder(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + repOrderEntity.getId()))
                .andExpect(status().isNoContent());

        boolean exists = repos.repOrder.existsById(repOrderEntity.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void givenValidRepId_whenFindIOJAssessorDetailsIsCalled_() throws Exception {
        String userName = "grea-k";
        UserEntity userEntity = TestEntityDataBuilder.getUserEntity();
        repos.user.save(userEntity);

        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(REP_ID);
        repOrder.setUserCreatedEntity(userEntity);
        repOrder.setUserCreated(userName);

        repos.repOrder.save(repOrder);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + REP_ID + "/ioj-assessor-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value(userName));

    }

    @Test
    void givenTooLargeAsk_whenFdcDelayedCalled_thenAllAvailableValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();


        mockMvc.perform(MockMvcRequestBuilders.get(buildFdcDelayedEndpointURL(5,LocalDate.now(), 5))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(repOrderValid.getId(), repOrderValid2.getId())));
    }

    @Test
    void givenSingleAsk_whenFdcDelayedCalled_thenOnlyOneValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        mockMvc.perform(MockMvcRequestBuilders.get(buildFdcDelayedEndpointURL(5,LocalDate.now(), 1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(repOrderValid.getId())));
    }

    @Test
    void givenTooLargeAsk_whenFdcFastTrackCalled_thenAllAvailableValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        mockMvc.perform(MockMvcRequestBuilders.get(buildFdcFastTrackEndpointURL(5, 0, LocalDate.now(), 5))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(repOrderFuture.getId(), repOrderFuture2.getId())));
    }

    @Test
    void givenSingleAsk_whenFdcFastTrackCalled_thenOnlyOneValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        mockMvc.perform(MockMvcRequestBuilders.get(buildFdcFastTrackEndpointURL(5, 0,LocalDate.now(), 1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(repOrderFuture.getId())));
    }
    private String buildFdcDelayedEndpointURL(int delayPeriod, LocalDate dateReceived, int numRecords){
        //delay=5&dateReceived=01.10.2010&numRecords=5
        String date = dateReceived.format(DateTimeFormatter.ISO_DATE);
        String parameterString = "?delay=%s&dateReceived=%s&numRecords=%s".formatted(delayPeriod, date, numRecords);
        return FDC_DELAYED_ENDPOINT_URL +parameterString;
    }

    private String buildFdcFastTrackEndpointURL(int delayPeriod, int startingMonth, LocalDate dateReceived, int numRecords){
        //delay=5&dateReceived=01.10.2010&numRecords=5&startingMonth=0
        String date = dateReceived.format(DateTimeFormatter.ISO_DATE);
        String parameterString = "?delay=%s&dateReceived=%s&numRecords=%s&startingMonth=%s".formatted(delayPeriod, date, numRecords, startingMonth);
        return FDC_FAST_TRACK_ENDPOINT_URL +parameterString;
    }
    private void setUpFdcMinDelayAppliesEntities() {
        LocalDate dateJan2010 = LocalDate.of(2010,1,1);
        LocalDate dateFuture = LocalDate.now().plus(1, ChronoUnit.MONTHS);

        // basic entity to satisfy basic MinDelayApplies criteria
        repOrderValid = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrderValid.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderValid, "ACQUITTAL"));


        // 2nd entity to allow for testing quantity for MinDelayApplies
        repOrderValid2 = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrderValid2.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderValid2, "ACQUITTAL"));

        // SCENARIOS:
        RepOrderEntity repOrder;
        // STATUS != 'SENT'
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.REPLACED));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // CCOO_OUTCOME IS NULL
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, null));

        // SENTENCE_ORDER_DATE IS NULL
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(null, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // SENTENCE_ORDER_DATE > NOW
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateFuture, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // DATE_RECEIVED<'01-JAN-2015'
        repOrderFuture = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateFuture));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrderFuture.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderFuture, "ACQUITTAL"));

        repOrderFuture2 = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateFuture));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(repOrderFuture2.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderFuture2, "ACQUITTAL"));
    }


}
