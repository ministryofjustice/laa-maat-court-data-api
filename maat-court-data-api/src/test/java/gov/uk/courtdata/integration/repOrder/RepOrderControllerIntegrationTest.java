package gov.uk.courtdata.integration.repOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.util.ApiHeaders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class RepOrderControllerIntegrationTest extends MockMvcIntegrationTest {

    public static final Integer INVALID_REP_ID = 9999;
    public static final Integer INVALID_MVO_ID = 8888;
    public static final String BASE_URL = "/api/internal/v1/assessment/rep-orders";
    public static final String SLASH = "/";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";
    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";
    private static final String FDC_DELAYED_ENDPOINT_URL =
            "/api/internal/v1/assessment/rep-orders?fdcDelayedPickup=true&delay={delay}&dateReceived={dateReceived}&numRecords={numRecords}";
    private static final String FDC_FAST_TRACK_ENDPOINT_URL =
            "/api/internal/v1/assessment/rep-orders?fdcFastTrack=true&delay={delay}&dateReceived={dateReceived}&numRecords={numRecords}";
    private static final String SEARCH_MAAT_APPLICATION =
            "/api/internal/v1/assessment/rep-orders/search-maat-application";
    private static final String CURRENT_REGISTRATION = "current-registration";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    private RepOrderEntity repOrderValid;
    private RepOrderEntity repOrderValid2;
    private RepOrderEntity repOrderFuture;
    private RepOrderEntity repOrderFuture2;
    public Integer repOrderIdNoSentenceOrderDate;
    public Integer repId;
    private static final String FIRST_NAME = "FirstName";

    @Autowired
    private RepOrderMapper mapper;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        Applicant applicant = repos.applicantRepository.save(TestEntityDataBuilder.getApplicant(1));
        RepOrderEntity repOrdTestData = TestEntityDataBuilder.getPopulatedRepOrder();
        repOrdTestData.setSentenceOrderDate(null);
        repOrdTestData.setArrestSummonsNo(TestEntityDataBuilder.ASN_NUMBER);
        repOrdTestData.setApplicationId(applicant.getId());
        repOrdTestData.setCatyCaseType("SUMMARY ONLY");
        RepOrderEntity repOrder = repos.repOrder.save(repOrdTestData);
        repOrderIdNoSentenceOrderDate = repOrder.getId();

        RepOrderEntity repOrderEntity = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        repId = repOrderEntity.getId();

        repos.repOrderMvo.save(TestEntityDataBuilder.getRepOrderMvoEntity(TestEntityDataBuilder.MVO_ID, repOrder));
        repos.repOrderMvoReg.save(
                TestEntityDataBuilder.getRepOrderMvoRegEntity(TestEntityDataBuilder.REP_ID, repOrder));
        WqLinkRegisterEntity linkRegisterEntity =
                TestEntityDataBuilder.getWQLinkRegisterEntity(TestEntityDataBuilder.REP_ID);
        linkRegisterEntity.setMaatId(repOrderIdNoSentenceOrderDate);
        repos.wqLinkRegister.save(linkRegisterEntity);

        repos.repOrderCPData.save(TestEntityDataBuilder.getRepOrderEntity(repOrderIdNoSentenceOrderDate));
    }

    private RepOrderDTO getUpdatedRepOrderDTO() {
        RepOrderEntity repOrderEntity = repos.repOrder.getReferenceById(repId);
        RepOrderDTO repOrderDTO = TestModelDataBuilder.getRepOrderDTO(repId);
        repOrderDTO.setDateModified(repOrderEntity.getDateModified());
        repOrderDTO.setSentenceOrderDate(repOrderEntity.getSentenceOrderDate());
        return repOrderDTO;
    }

    @Test
    void givenInvalidRepId_whenFindInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertThat(runNotFoundErrorScenario(
                        "No Rep Order found for ID: " + INVALID_REP_ID, get(BASE_URL + SLASH + INVALID_REP_ID)))
                .isTrue();
    }

    @Test
    void givenIncorrectRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        assertThat(runNotFoundErrorScenario(
                        "No Rep Order found for ID: " + repOrderIdNoSentenceOrderDate,
                        get(BASE_URL + SLASH + repOrderIdNoSentenceOrderDate + "?has_sentence_order_date=true")))
                .isTrue();
    }

    @Test
    void givenValidRepId_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        var response = runSuccessScenario(get(BASE_URL + SLASH + repId));
        softly.assertThat(response.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(repOrderDTO));
        softly.assertThat(response.getResponse().getHeader(ApiHeaders.TOTAL_RECORDS))
                .isEqualTo("1");
    }

    @Test
    void givenValidRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        var response = runSuccessScenario(get(BASE_URL + SLASH + repId + "?has_sentence_order_date=true"));
        softly.assertThat(response.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(repOrderDTO));
        softly.assertThat(response.getResponse().getHeader(ApiHeaders.TOTAL_RECORDS))
                .isEqualTo("1");
    }

    @Test
    void givenRepIdIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        "Rep Id is missing from request and is required",
                        post(BASE_URL + SLASH + "update-date-completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        UpdateAppDateCompleted.builder().build()))))
                .isTrue();
    }

    @Test
    void givenDateIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        "Assessment Date completed is missing from request and is required",
                        post(BASE_URL + SLASH + "update-date-completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(UpdateAppDateCompleted.builder()
                                        .repId(repId)
                                        .build()))))
                .isTrue();
    }

    @Test
    void givenInvalidRepId_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        "MAAT/REP ID [" + INVALID_REP_ID + "] is invalid",
                        post(BASE_URL + SLASH + "update-date-completed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(UpdateAppDateCompleted.builder()
                                        .repId(INVALID_REP_ID)
                                        .assessmentDateCompleted(LocalDateTime.now())
                                        .build()))))
                .isTrue();
    }

    @Test
    void givenValidParameters_whenUpdateAppDateCompletedIsInvoked_theCompletedDateShouldUpdate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter)
                .toLocalDate();

        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + SLASH + "update-date-completed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson(repId))
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repos.repOrder.getReferenceById(repId);
        assertThat(repOrderEntity.getId()).isEqualTo(repId);
        assertThat(repOrderEntity.getAssessmentDateCompleted()).isEqualTo(expectedDate);
    }

    @Test
    void givenInvalidMvoId_whenFindByCurrentRegistrationIsInvoked_thenCorrectErrorResponseIsReturned()
            throws Exception {
        assertThat(runNotFoundErrorScenario(
                        "No Rep Order MVO Reg found for ID: " + INVALID_MVO_ID,
                        get(MVO_REG_ENDPOINT_URL + "/" + INVALID_MVO_ID + "/" + CURRENT_REGISTRATION)))
                .isTrue();
    }

    @Test
    void givenValidMvoId_whenFindByCurrentRegistrationIsInvoked_thenRepOrderMvoRegIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        List.of(TestModelDataBuilder.getRepOrderMvoRegDTO()),
                        get(MVO_REG_ENDPOINT_URL + "/" + TestEntityDataBuilder.MVO_ID + "/" + CURRENT_REGISTRATION)))
                .isTrue();
    }

    @Test
    void givenInvalidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenCorrectErrorResponseIsReturned()
            throws Exception {
        assertThat(runNotFoundErrorScenario(
                        "No Rep Order MVO found for ID: " + INVALID_REP_ID,
                        get(MVO_ENDPOINT_URL + "/" + INVALID_REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES)))
                .isTrue();
    }

    @Test
    void givenValidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        TestModelDataBuilder.getRepOrderMvoDTO(
                                TestModelDataBuilder.MVO_ID, repOrderIdNoSentenceOrderDate),
                        get(MVO_ENDPOINT_URL + "/" + repOrderIdNoSentenceOrderDate + "?owner="
                                + VEHICLE_OWNER_INDICATOR_YES)))
                .isTrue();
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
        assertThat(runBadRequestErrorScenario(
                        "MAAT/REP ID is required, found [null]",
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        UpdateRepOrder.builder().build()))))
                .isTrue();
    }

    @Test
    void givenInvalidRepId_whenUpdateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        "MAAT/REP ID [" + INVALID_REP_ID + "] is invalid",
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(UpdateRepOrder.builder()
                                        .repId(INVALID_REP_ID)
                                        .build()))))
                .isTrue();
    }

    @Test
    void givenValidParameters_whenUpdateIsInvoked_theRepOrderIsUpdated() throws Exception {

        UpdateRepOrder request = TestModelDataBuilder.getUpdateRepOrder(repId);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getRepId()))
                .andExpect(jsonPath("$.userModified").value(request.getUserModified()))
                .andExpect(jsonPath("$.crownRepOrderDecision").value(request.getCrownRepOrderDecision()));
    }

    @Test
    void givenValidRepId_whenDeleteIsInvoked_theRepOrderIsDeleted() throws Exception {

        RepOrderEntity repOrderEntity = TestEntityDataBuilder.getPopulatedRepOrder(repId);

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

        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(repId);
        repOrder.setUserCreatedEntity(userEntity);
        repOrder.setUserCreated(userName);

        repos.repOrder.save(repOrder);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + repId + "/ioj-assessor-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value(userName));
    }

    @Test
    void givenTooLargeAsk_whenFdcDelayedCalled_thenAllAvailableValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        MvcResult result = mockMvc.perform(
                        get(FDC_DELAYED_ENDPOINT_URL, 5, LocalDate.now(), 5).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        List<Integer> ids =
                JsonPath.parse(result.getResponse().getContentAsString()).read("$");
        assertThat(ids).containsExactlyInAnyOrder(repOrderValid.getId(), repOrderValid2.getId());
    }

    @Test
    void givenSingleAsk_whenFdcDelayedCalled_thenOnlyOneValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        MvcResult result = mockMvc.perform(
                        get(FDC_DELAYED_ENDPOINT_URL, 5, LocalDate.now(), 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        List<Integer> ids =
                JsonPath.parse(result.getResponse().getContentAsString()).read("$");
        assertThat(ids).containsExactlyInAnyOrder(repOrderValid.getId());
    }

    @Test
    void givenTooLargeAsk_whenFdcFastTrackCalled_thenAllAvailableValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        MvcResult result = mockMvc.perform(
                        get(FDC_FAST_TRACK_ENDPOINT_URL, 5, LocalDate.now(), 5).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        List<Integer> ids =
                JsonPath.parse(result.getResponse().getContentAsString()).read("$");
        assertThat(ids).containsExactlyInAnyOrder(repOrderFuture.getId(), repOrderFuture2.getId());
    }

    @Test
    void givenSingleAsk_whenFdcFastTrackCalled_thenOnlyOneValidRepOrdersReturned() throws Exception {
        setUpFdcMinDelayAppliesEntities();
        MvcResult result = mockMvc.perform(
                        get(FDC_FAST_TRACK_ENDPOINT_URL, 5, LocalDate.now(), 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();

        List<Integer> ids =
                JsonPath.parse(result.getResponse().getContentAsString()).read("$");
        assertThat(ids).containsExactlyInAnyOrder(repOrderFuture.getId());
    }

    private void setUpFdcMinDelayAppliesEntities() {
        LocalDate dateJan2010 = LocalDate.of(2010, 1, 1);
        LocalDate dateFuture = LocalDate.now().plus(1, ChronoUnit.MONTHS);

        // basic entity to satisfy basic MinDelayApplies criteria
        repOrderValid = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(
                repOrderValid.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderValid, "ACQUITTAL"));

        // 2nd entity to allow for testing quantity for MinDelayApplies
        repOrderValid2 = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(
                repOrderValid2.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderValid2, "ACQUITTAL"));

        // SCENARIOS:
        RepOrderEntity repOrder;
        // STATUS != 'SENT'
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(
                repOrder.getId(), ConcorContributionStatus.REPLACED));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // CCOO_OUTCOME IS NULL
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateJan2010));
        repos.concorContributions.save(
                TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, null));

        // SENTENCE_ORDER_DATE IS NULL
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(null, dateJan2010));
        repos.concorContributions.save(
                TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // SENTENCE_ORDER_DATE > NOW
        repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateFuture, dateJan2010));
        repos.concorContributions.save(
                TestEntityDataBuilder.getConcorContributionsEntity(repOrder.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrder, "ACQUITTAL"));

        // DATE_RECEIVED<'01-JAN-2015'
        repOrderFuture = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateFuture));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(
                repOrderFuture.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderFuture, "ACQUITTAL"));

        repOrderFuture2 = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(dateJan2010, dateFuture));
        repos.concorContributions.save(TestEntityDataBuilder.getConcorContributionsEntity(
                repOrderFuture2.getId(), ConcorContributionStatus.SENT));
        repos.crownCourtProcessing.save(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(repOrderFuture2, "ACQUITTAL"));
    }

    @Test
    void givenAEmptyContent_whenSearchApplicationIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAInvalidInput_whenSearchApplicationIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
                        .content(TestModelDataBuilder.getMaatSearchRequestJson("Invalid_FirstName"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Representation order not found"));
    }

    @Test
    void givenAValidInput_whenSearchApplicationIsInvoked_thenCorrectResponseIsReturned() throws Exception {

        expectLinkedSearchResult(mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
                .content(TestModelDataBuilder.getMaatSearchRequestJsonWithNullASN(FIRST_NAME))
                .contentType(MediaType.APPLICATION_JSON)));

        //        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
        //                        .content(TestModelDataBuilder.getMaatSearchRequestJson("FirstName"))
        //                        .contentType(MediaType.APPLICATION_JSON))
        //                .andExpect(status().isOk())
        //                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        //                .andExpect(jsonPath("$[0].maatId").value(repOrderIdNoSentenceOrderDate))
        //                .andExpect(jsonPath("$[0].isLinked").value(Boolean.TRUE))
        //                .andExpect(jsonPath("$[0].linkingDetail.caseUrn").value(TestEntityDataBuilder.CASE_URN))
        //                .andExpect(jsonPath("$[0].linkingDetail.libraId").value(TestEntityDataBuilder.LIBRA_ID))
        //                .andExpect(jsonPath("$[0].linkingDetail.caseId").value(TestEntityDataBuilder.TEST_CASE_ID));
    }

    @Test
    void givenDateModifiedInPatchRequest_whenRepOrderUpdated_thenResponseContainsGeneratedDateModified()
            throws Exception {
        // given
        LocalDateTime staleDateModified = LocalDateTime.now().minusDays(1);

        Map<String, Object> request =
                Map.of("dateModified", staleDateModified.toString(), "arrestSummonsNo", "UPDATED_ASN");

        // when
        String response = mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + SLASH + repId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrestSummonsNo").value("UPDATED_ASN"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        RepOrderDTO repOrderResponse = objectMapper.readValue(response, RepOrderDTO.class);

        // then
        RepOrderEntity persistedRepOrder = repos.repOrder.findById(repId).orElseThrow();

        softly.assertThat(repOrderResponse.getDateModified()).isEqualTo(persistedRepOrder.getDateModified());

        softly.assertThat(repOrderResponse.getDateModified()).isNotEqualTo(staleDateModified);
    }

    @Test
    void givenRepOrderPatchRequest_whenRepOrderUpdated_thenResponseContainsLatestPersistedDateModified()
            throws Exception {
        // given
        RepOrderEntity originalRepOrder = repos.repOrder.findById(repId).orElseThrow();
        LocalDateTime originalDateModified = originalRepOrder.getDateModified();

        Map<String, Object> request = Map.of("arrestSummonsNo", "UPDATED_ASN");

        // when
        String response = mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + SLASH + repId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrestSummonsNo").value("UPDATED_ASN"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        RepOrderDTO repOrderResponse = objectMapper.readValue(response, RepOrderDTO.class);

        // then
        RepOrderEntity persistedRepOrder = repos.repOrder.findById(repId).orElseThrow();

        softly.assertThat(repOrderResponse.getDateModified()).isEqualTo(persistedRepOrder.getDateModified());

        softly.assertThat(repOrderResponse.getDateModified()).isAfterOrEqualTo(originalDateModified);
    }

    //    @Test
    //    void givenNullAsn_whenSearchApplicationIsInvoked_thenCorrectResponseIsReturned1() throws Exception {
    //
    //        mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
    //                        .content(TestModelDataBuilder.getMaatSearchRequestJsonWithNullASN(FIRST_NAME))
    //                        .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isOk())
    //                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(jsonPath("$[0].maatId").value(repOrderIdNoSentenceOrderDate))
    //                .andExpect(jsonPath("$[0].isLinked").value(Boolean.TRUE))
    //                .andExpect(jsonPath("$[0].linkingDetail.caseUrn").value(TestEntityDataBuilder.CASE_URN))
    //                .andExpect(jsonPath("$[0].linkingDetail.libraId").value(TestEntityDataBuilder.LIBRA_ID))
    //                .andExpect(jsonPath("$[0].linkingDetail.caseId").value(TestEntityDataBuilder.TEST_CASE_ID));
    //    }

    @Test
    void givenNullAsn_whenSearchApplicationIsInvoked_thenCorrectResponseIsReturned() throws Exception {
        expectLinkedSearchResult(mockMvc.perform(MockMvcRequestBuilders.post(SEARCH_MAAT_APPLICATION)
                .content(TestModelDataBuilder.getMaatSearchRequestJsonWithNullASN(FIRST_NAME))
                .contentType(MediaType.APPLICATION_JSON)));
    }

    private void expectLinkedSearchResult(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].maatId").value(repOrderIdNoSentenceOrderDate))
                .andExpect(jsonPath("$[0].isLinked").value(Boolean.TRUE))
                .andExpect(jsonPath("$[0].linkingDetail.caseUrn").value(TestEntityDataBuilder.CASE_URN))
                .andExpect(jsonPath("$[0].linkingDetail.libraId").value(TestEntityDataBuilder.LIBRA_ID))
                .andExpect(jsonPath("$[0].linkingDetail.caseId").value(TestEntityDataBuilder.TEST_CASE_ID));
    }
}
