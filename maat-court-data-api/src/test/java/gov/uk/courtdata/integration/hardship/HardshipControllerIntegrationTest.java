package gov.uk.courtdata.integration.hardship;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.DataBuilderUtil;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailReason;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.SolicitorCosts;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.enums.HardshipReviewStatus;

@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {MAATCourtDataApplication.class}
)
class HardshipControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String TEST_USER = "test-s";
    private final String BASE_URL = "/api/internal/v1/assessment/hardship";
    private final String HARDSHIP_URL = BASE_URL + "/{hardshipId}";
    private final String HARDSHIP_BY_REP_ID_URL = BASE_URL + "/repId/{repId}";
    private Integer MOCK_REP_ID_2 = 5555;

    private HardshipReviewEntity existingHardshipReview;
    private FinancialAssessmentEntity existingFinancialAssessment;
    private FinancialAssessmentEntity existingUnlinkedFinancialAssessment;
    private NewWorkReasonEntity existingNewWorkReason;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        setupTestData();
    }

    @Test
    void givenAHardshipReviewIdThatDoesNotExist_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        int invalidHardshipId = 9999;
        assertTrue(runBadRequestErrorScenario(String.format("%d is invalid", invalidHardshipId),
                get(HARDSHIP_URL, invalidHardshipId)
        ));
    }

    @Test
    void givenAZeroHardshipReviewId_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Hardship review id is required", get(HARDSHIP_URL, 0)));
    }

    @Test
    void givenAValidHardshipReviewId_whenGetHardshipIsInvoked_theCorrectDataIsReturned() throws Exception {
        assertTrue(runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_URL, existingHardshipReview.getId())
        ));
    }

    @Test
    void givenAnInvalidRepId_whenGetHardshipByRepIdIsInvoked_theCorrectErrorIsReturned() throws Exception {
        Integer repId = 9999;
        assertTrue(runNotFoundErrorScenario(String.format("No Hardship Review found for REP ID: %s", repId),
                get(HARDSHIP_BY_REP_ID_URL, repId)
        ));
    }

    @Test
    void givenAValidRepID_whenGetHardshipByRepIdIsInvoked_theCorrectDataIsReturned() throws Exception {
        assertTrue(runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_BY_REP_ID_URL, existingHardshipReview.getRepId())
        ));
    }

    @Test
    void givenAValidHardshipReview_whenCreateHardshipIsInvoked_theCorrectDataIsPersisted() throws Exception {
        CreateHardshipReview request = CreateHardshipReview.builder()
                .financialAssessmentId(existingUnlinkedFinancialAssessment.getId())
                .repId(existingUnlinkedFinancialAssessment.getRepOrder().getId())
                .nworCode(existingNewWorkReason.getCode())
                .cmuId(existingUnlinkedFinancialAssessment.getCmuId())
                .reviewResult("FAIL")
                .reviewDate(LocalDateTime.now())
                .resultDate(LocalDateTime.now())
                .solicitorCosts(SolicitorCosts.builder()
                        .rate(DataBuilderUtil.createScaledBigDecimal(1.23))
                        .hours(DataBuilderUtil.createScaledBigDecimal(12.00))
                        .vat(DataBuilderUtil.createScaledBigDecimal(123.45))
                        .disbursements(DataBuilderUtil.createScaledBigDecimal(0.00))
                        .estimatedTotal(DataBuilderUtil.createScaledBigDecimal(345.67)).build())
                .disposableIncome(DataBuilderUtil.createScaledBigDecimal(13000.00))
                .disposableIncomeAfterHardship(DataBuilderUtil.createScaledBigDecimal(3000.00))
                .status(HardshipReviewStatus.COMPLETE)
                .userCreated(TEST_USER)
                .courtType("MAGISTRATE")
                .reviewDetails(List.of(
                        getTestHardshipReviewDetail(null, null, getTestHardshipReviewDetailReason())))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.newWorkReason").value(request.getNworCode()))
                .andExpect(jsonPath("$.reviewResult").value(request.getReviewResult()))
                .andExpect(jsonPath("$.status").value(request.getStatus().getStatus()));
    }

    @Test
    void givenAValidHardshipReview_whenUpdateHardshipIsInvoked_theCorrectDataIsPersisted() throws Exception {

        HardshipReviewDetailEntity existingReviewDetails = existingHardshipReview.getReviewDetails().get(0);
        HardshipReviewDetail updatedReviewDetails = getTestHardshipReviewDetail(
                existingReviewDetails.getId(), existingReviewDetails.getDateCreated(),
                getTestHardshipReviewDetailReason()
        );
        updatedReviewDetails.setFrequency(Frequency.TWO_WEEKLY);
        updatedReviewDetails.setAmount(DataBuilderUtil.createScaledBigDecimal(250.00));

        UpdateHardshipReview request = UpdateHardshipReview.builder()
                .id(existingHardshipReview.getId())
                .nworCode(existingHardshipReview.getNewWorkReason().getCode())
                .cmuId(existingFinancialAssessment.getCmuId())
                .reviewResult("FAIL")
                .reviewDate(LocalDateTime.now())
                .resultDate(LocalDateTime.now())
                .solicitorCosts(SolicitorCosts.builder()
                        .rate(DataBuilderUtil.createScaledBigDecimal(1.23))
                        .hours(DataBuilderUtil.createScaledBigDecimal(12.00))
                        .vat(DataBuilderUtil.createScaledBigDecimal(123.45))
                        .disbursements(DataBuilderUtil.createScaledBigDecimal(0.00))
                        .estimatedTotal(DataBuilderUtil.createScaledBigDecimal(345.67)).build())
                .disposableIncome(DataBuilderUtil.createScaledBigDecimal(13000.00))
                .disposableIncomeAfterHardship(DataBuilderUtil.createScaledBigDecimal(3000.00))
                .status(HardshipReviewStatus.COMPLETE)
                .reviewDetails(List.of(updatedReviewDetails))
                .updated(existingHardshipReview.getUpdated())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.newWorkReason").value(request.getNworCode()))
                .andExpect(jsonPath("$.reviewResult").value(request.getReviewResult()))
                .andExpect(jsonPath("$.status").value(request.getStatus().getValue()));
    }

    private void assertPersistedHardshipReviewDataIsCorrectOnCreation(CreateHardshipReview inputReviewData, HardshipReviewEntity createdHardshipReviewEntity) {
        assertPersistedHardshipReviewDataIsCorrect(inputReviewData, createdHardshipReviewEntity);
        assertThat(createdHardshipReviewEntity.getFinancialAssessmentId()).isEqualTo(inputReviewData.getFinancialAssessmentId());
        assertThat(createdHardshipReviewEntity.getUserCreated()).isEqualTo(inputReviewData.getUserCreated());
        assertThat(createdHardshipReviewEntity.getRepId()).isEqualTo(inputReviewData.getRepId());
        assertThat(createdHardshipReviewEntity.getCourtType()).isEqualTo(inputReviewData.getCourtType());
    }

    private void assertPersistedHardshipReviewDataIsCorrectOnUpdate(UpdateHardshipReview inputReviewData, HardshipReviewEntity updatedHardshipReview) {
        assertPersistedHardshipReviewDataIsCorrect(inputReviewData, updatedHardshipReview);
        assertThat(updatedHardshipReview.getUserModified()).isEqualTo(inputReviewData.getUserModified());
        assertThat(updatedHardshipReview.getUpdated()).isNotEqualTo(inputReviewData.getUpdated());
    }

    private <T extends HardshipReview> void assertPersistedHardshipReviewDataIsCorrect(T inputReviewData, HardshipReviewEntity hardshipReviewEntity) {

        assertThat(hardshipReviewEntity).isNotNull();
        assertThat(hardshipReviewEntity.getNewWorkReason().getCode()).isEqualTo(inputReviewData.getNworCode());

        assertThat(hardshipReviewEntity.getCmuId()).isEqualTo(inputReviewData.getCmuId());
        assertThat(hardshipReviewEntity.getReviewDate()).isEqualTo(inputReviewData.getReviewDate());
        assertThat(hardshipReviewEntity.getResultDate()).isEqualTo(inputReviewData.getResultDate());
        assertThat(hardshipReviewEntity.getStatus()).isEqualTo(inputReviewData.getStatus().getStatus());
        assertThat(hardshipReviewEntity.getSolicitorDisb()).isEqualTo(inputReviewData.getSolicitorCosts().getDisbursements());
        assertThat(hardshipReviewEntity.getSolicitorEstTotalCost()).isEqualTo(inputReviewData.getSolicitorCosts().getEstimatedTotal());
        assertThat(hardshipReviewEntity.getSolicitorHours()).isEqualTo(inputReviewData.getSolicitorCosts().getHours());
        assertThat(hardshipReviewEntity.getSolicitorRate()).isEqualTo(inputReviewData.getSolicitorCosts().getRate());
        assertThat(hardshipReviewEntity.getSolicitorVat()).isEqualTo(inputReviewData.getSolicitorCosts().getVat());
        assertThat(hardshipReviewEntity.getReviewResult()).isEqualTo(inputReviewData.getReviewResult());
        assertThat(hardshipReviewEntity.getDisposableIncomeAfterHardship()).isEqualTo(inputReviewData.getDisposableIncomeAfterHardship());
        assertThat(hardshipReviewEntity.getDisposableIncome()).isEqualTo(inputReviewData.getDisposableIncome());

        List<HardshipReviewDetailEntity> reviewDetails = repos.hardshipReviewDetail.findAllByHardshipReviewId(
            hardshipReviewEntity.getId());
        assertThat(reviewDetails).hasSameSizeAs(inputReviewData.getReviewDetails());

        for (int i = 0; i < reviewDetails.size(); i++) {
            HardshipReviewDetail expectedReviewDetails = inputReviewData.getReviewDetails().get(i);
            HardshipReviewDetailEntity persistedReviewDetails = reviewDetails.get(i);

            assertThat(persistedReviewDetails.getDetailType()).isEqualTo(expectedReviewDetails.getDetailType());
            assertThat(persistedReviewDetails.getUserCreated()).isEqualTo(expectedReviewDetails.getUserCreated());
            assertThat(persistedReviewDetails.getFrequency()).isEqualTo(expectedReviewDetails.getFrequency());
            assertThat(persistedReviewDetails.getOtherDescription()).isEqualTo(
                    expectedReviewDetails.getOtherDescription());
            assertThat(persistedReviewDetails.getAmount()).isEqualTo(expectedReviewDetails.getAmount());
            assertThat(persistedReviewDetails.getAccepted()).isEqualTo(expectedReviewDetails.getAccepted());
            assertThat(persistedReviewDetails.getActive()).isEqualTo(expectedReviewDetails.getActive() ? YES : NO);
            assertThat(persistedReviewDetails.getDetailReason().getReason()).isEqualTo(
                    expectedReviewDetails.getDetailReason().getReason());
        }
    }

    private boolean runCreateHardshipReviewErrorScenario(String errorMessage, CreateHardshipReview body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private boolean runUpdateHardshipReviewErrorScenario(String errorMessage, UpdateHardshipReview body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }


    private void setupTestData() {
        existingNewWorkReason = repos.mockNewWorkReason.save(
                TestEntityDataBuilder.getNewWorkReasonEntity());

        RepOrderEntity repOrderEntity = repos.repOrder.save(
            TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderEntity repOrderForUnlink = repos.repOrder.save(
            TestEntityDataBuilder.getPopulatedRepOrder(MOCK_REP_ID_2));
        MOCK_REP_ID_2 = repOrderForUnlink.getId();

        existingFinancialAssessment = repos.financialAssessment.save(
            getTestFinancialAssessment(repOrderEntity));
        existingUnlinkedFinancialAssessment = repos.financialAssessment.save(
            getTestFinancialAssessment(repOrderForUnlink));
        existingHardshipReview = repos.hardshipReview.save(
                getTestHardshipReview(existingFinancialAssessment.getRepOrder().getId(), existingFinancialAssessment.getId()));

    }

    private HardshipReviewEntity getTestHardshipReview(Integer repId, Integer supportingAssessmentId) {
        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReview.addReviewDetail(getTestHardshipReviewDetailEntity());
        hardshipReview.setFinancialAssessmentId(supportingAssessmentId);
        hardshipReview.setRepId(repId);
        hardshipReview.setStatus(HardshipReviewStatus.IN_PROGRESS.getStatus());
        return hardshipReview;
    }

    private HardshipReviewDetailEntity getTestHardshipReviewDetailEntity() {
        HardshipReviewDetailEntity hardshipReviewDetail = TestEntityDataBuilder.getHardshipReviewDetailsEntity();
        hardshipReviewDetail.setId(null);
        return hardshipReviewDetail;
    }

    private HardshipReviewDTO getTestHardshipReviewDTO() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTO();
        hardshipReviewDTO.setId(existingHardshipReview.getId());
        hardshipReviewDTO.setUpdated(existingHardshipReview.getUpdated());
        hardshipReviewDTO.setStatus(HardshipReviewStatus.IN_PROGRESS);

        HardshipReviewDetailEntity existingReviewDetails = existingHardshipReview.getReviewDetails().get(0);

        hardshipReviewDTO.setReviewDetails(List.of(
                getTestHardshipReviewDetail(
                        existingReviewDetails.getId(), existingReviewDetails.getDateModified(),
                        getTestHardshipReviewDetailReason()
                )));
        return hardshipReviewDTO;
    }

    private HardshipReviewDetail getTestHardshipReviewDetail(Integer id, LocalDateTime dateModified,
                                                             HardshipReviewDetailReason detailReason) {
        HardshipReviewDetail reviewDetail = TestModelDataBuilder.getHardshipReviewDetail();

        reviewDetail.setId(id);
        reviewDetail.setDateModified(dateModified);

        if (detailReason != null) {
            reviewDetail.setDetailReason(detailReason);
        }

        return reviewDetail;
    }

    private HardshipReviewDetailReason getTestHardshipReviewDetailReason() {
//        return HardshipReviewDetailReason.getFrom(existingHardshipReviewDetailReason.getReason());
        return HardshipReviewDetailReason.ALLOWABLE_EXPENSE;
    }

    private FinancialAssessmentEntity getTestFinancialAssessment(RepOrderEntity repOrderEntity) {
        return FinancialAssessmentEntity.builder()
                .repOrder(repOrderEntity)
                .initialAscrId(1)
                .userCreated(TEST_USER)
                .cmuId(1)
                .newWorkReason(existingNewWorkReason)
                .dateCompleted(LocalDateTime.now())
                .replaced("N")
                .fullAssessmentDate(LocalDateTime.now())
                .build();
    }

}
