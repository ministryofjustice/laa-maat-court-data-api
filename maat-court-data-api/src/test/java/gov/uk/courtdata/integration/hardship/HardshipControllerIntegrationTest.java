package gov.uk.courtdata.integration.hardship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.DataBuilderUtil;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailType;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.integration.MockCdaWebConfig;
import gov.uk.courtdata.integration.MockNewWorkReasonRepository;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.*;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {MAATCourtDataApplication.class, MockCdaWebConfig.class})
public class HardshipControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String TEST_USER = "test-s";
    private final String BASE_URL = "/api/internal/v1/assessment/hardship";
    private final String HARDSHIP_URL = BASE_URL + "/{hardshipId}";
    private final String HARDSHIP_BY_REP_ID_URL = BASE_URL + "/repId/{repId}";

    @Autowired
    private HardshipReviewRepository hardshipReviewRepository;
    @Autowired
    private HardshipReviewDetailRepository hardshipReviewDetailRepository;
    @Autowired
    private HardshipReviewDetailReasonRepository hardshipReviewDetailReasonRepository;
    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;
    @Autowired
    private MockNewWorkReasonRepository mockNewWorkReasonRepository;

    private HardshipReviewEntity existingHardshipReview;
    private HardshipReviewDetailReasonEntity existingHardshipReviewDetailReason;
    private FinancialAssessmentEntity existingFinancialAssessment;
    private FinancialAssessmentEntity existingUnlinkedFinancialAssessment;
    private NewWorkReasonEntity existingNewWorkReason;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        hardshipReviewRepository.deleteAll();
        hardshipReviewDetailRepository.deleteAll();
        hardshipReviewDetailReasonRepository.deleteAll();
        financialAssessmentRepository.deleteAll();
        mockNewWorkReasonRepository.deleteAll();

        setupTestData();
    }

    @Test
    public void givenAHardshipReviewIdThatDoesNotExist_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        int invalidHardshipId = 9999;
        runBadRequestErrorScenario(String.format("%d is invalid", invalidHardshipId), get(HARDSHIP_URL, invalidHardshipId));
    }

    @Test
    public void givenAZeroHardshipReviewId_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        runBadRequestErrorScenario("Hardship review id is required", get(HARDSHIP_URL, 0));
    }

    @Test
    public void givenAValidHardshipReviewId_whenGetHardshipIsInvoked_theCorrectDataIsReturned() throws Exception {
        runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_URL, existingHardshipReview.getId()));
    }

    @Test
    public void givenAnInvalidRepId_whenGetHardshipByRepIdIsInvoked_theCorrectErrorIsReturned() throws Exception {
        Integer repId = 9999;
        runNotFoundErrorScenario(String.format("No Hardship Review found for REP ID: %s", repId), get(HARDSHIP_BY_REP_ID_URL, repId));
    }

    @Test
    public void givenAValidRepID_whenGetHardshipByRepIdIsInvoked_theCorrectDataIsReturned() throws Exception {
        runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_BY_REP_ID_URL, existingHardshipReview.getRepId()));
    }

    @Test
    public void givenAHardshipReviewWithNoSupportingAssessment_whenCreateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        runCreateHardshipReviewErrorScenario(
                "Review can only be entered after a completed assessment",
                CreateHardshipReview.builder().repId(9999).build());
    }

    @Test
    public void givenAHardshipReviewWhereReviewPredatesAssessment_whenCreateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        runCreateHardshipReviewErrorScenario(
                "Review date cannot pre-date the means assessment date",
                CreateHardshipReview.builder()
                        .repId(existingFinancialAssessment.getRepId())
                        .reviewDate(LocalDateTime.of(2022, 1, 1, 0, 0, 0))
                        .build());
    }

    @Test
    public void givenAValidHardshipReview_whenCreateHardshipIsInvoked_theCorrectDataIsPersisted() throws Exception {
        CreateHardshipReview body = CreateHardshipReview.builder()
                .financialAssessmentId(existingUnlinkedFinancialAssessment.getId())
                .repId(existingUnlinkedFinancialAssessment.getRepId())
                .nworCode(existingNewWorkReason.getCode())
                .cmuId(existingUnlinkedFinancialAssessment.getCmuId())
                .reviewResult("FAIL")
                .reviewDate(LocalDateTime.now())
                .resultDate(LocalDateTime.now())
                .solicitorCosts(SolicitorCosts.builder()
                        .solicitorRate(DataBuilderUtil.createScaledBigDecimal(1.23))
                        .solicitorHours(DataBuilderUtil.createScaledBigDecimal(12.00))
                        .solicitorVat(DataBuilderUtil.createScaledBigDecimal(123.45))
                        .solicitorDisb(DataBuilderUtil.createScaledBigDecimal(0.00))
                        .solicitorEstTotalCost(DataBuilderUtil.createScaledBigDecimal(345.67)).build())
                .disposableIncome(DataBuilderUtil.createScaledBigDecimal(13000.00))
                .disposableIncomeAfterHardship(DataBuilderUtil.createScaledBigDecimal(3000.00))
                .status(HardshipReviewStatus.COMPLETE)
                .userCreated(TEST_USER)
                .courtType("MAGISTRATE")
                .reviewDetails(List.of(
                        getTestHardshipReviewDetail(null, null, getTestHardshipReviewDetailReason())))
                .build();

        HardshipReviewDTO expectedResponse = HardshipReviewDTO.builder()
                .repId(body.getRepId())
                .cmuId(body.getCmuId())
                .newWorkReason(NewWorkReason.builder().code(body.getNworCode()).build())
                .reviewDate(body.getReviewDate())
                .reviewResult(body.getReviewResult())
                .financialAssessmentId(body.getFinancialAssessmentId())
                .resultDate(body.getResultDate())
                .userCreated(body.getUserCreated())
                .courtType(body.getCourtType())
                .reviewDetails(body.getReviewDetails())
                .solicitorCosts(body.getSolicitorCosts())
                .disposableIncome(body.getDisposableIncome())
                .disposableIncomeAfterHardship(body.getDisposableIncomeAfterHardship())
                .status(body.getStatus())
                .build();

        MvcResult result =
                runSuccessScenario(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        HardshipReviewEntity createdHardshipReviewEntity = hardshipReviewRepository.findByRepId(body.getRepId());

        assertPersistedHardshipReviewDataIsCorrectOnCreation(body, createdHardshipReviewEntity);

        // Align date/id values.
        expectedResponse.setUpdated(createdHardshipReviewEntity.getUpdated());
        expectedResponse.setDateCreated(createdHardshipReviewEntity.getDateCreated());

        List<HardshipReviewDetailEntity> reviewDetailEntities = createdHardshipReviewEntity.getReviewDetails();

        for (int i = 0; i < reviewDetailEntities.size(); i++) {
            expectedResponse.getReviewDetails().get(i).setId(reviewDetailEntities.get(i).getId());
            expectedResponse.getReviewDetails().get(i).setDateModified(reviewDetailEntities.get(i).getDateModified());
        }

        expectedResponse.setId(createdHardshipReviewEntity.getId());

        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenAHardshipReviewWithAZeroId_whenUpdateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        Integer hardshipId = 0;
        runUpdateHardshipReviewErrorScenario(
                "Hardship review id is required",
                UpdateHardshipReview.builder().id(hardshipId).build());
    }

    @Test
    public void givenAHardshipReviewIdThatDoesNotExist_whenUpdateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        Integer hardshipId = 9999;
        runUpdateHardshipReviewErrorScenario(
                String.format("%d is invalid", hardshipId),
                UpdateHardshipReview.builder().id(hardshipId).build());
    }

    @Test
    public void givenAnAlreadyCompletedHardshipReview_whenUpdateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        HardshipReviewEntity completedHardshipReview = hardshipReviewRepository.save(
                HardshipReviewEntity.builder()
                        .status(HardshipReviewStatus.COMPLETE)
                        .newWorkReason(existingNewWorkReason)
                        .userCreated(TEST_USER)
                        .dateCreated(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                        .build());

        runUpdateHardshipReviewErrorScenario(
                "User cannot modify a complete hardship review",
                UpdateHardshipReview.builder().id(completedHardshipReview.getId()).updated(existingHardshipReview.getUpdated()).build());
    }

    @Test
    public void givenAHardshipReviewThatHasBeenModifiedByAnotherUser_whenUpdateHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        LocalDateTime testDateTime = LocalDateTime.now();
        HardshipReviewEntity completedHardshipReview = hardshipReviewRepository.save(
                HardshipReviewEntity.builder()
                        .status(HardshipReviewStatus.COMPLETE)
                        .newWorkReason(existingNewWorkReason)
                        .userCreated(TEST_USER)
                        .updated(testDateTime)
                        .repId(existingFinancialAssessment.getRepId())
                        .build());

        runUpdateHardshipReviewErrorScenario(
                "Hardship has been modified by another user",
                UpdateHardshipReview.builder().id(completedHardshipReview.getId()).updated(testDateTime.minusMinutes(20)).build());
    }

    @Test
    public void givenAValidHardshipReview_whenUpdateHardshipIsInvoked_theCorrectDataIsPersisted() throws Exception {

        HardshipReviewDetailEntity existingReviewDetails = existingHardshipReview.getReviewDetails().get(0);
        HardshipReviewDetail updatedReviewDetails = getTestHardshipReviewDetail(
                existingReviewDetails.getId(), existingReviewDetails.getDateCreated(), getTestHardshipReviewDetailReason());
        updatedReviewDetails.setFrequency(Frequency.TWO_WEEKLY);
        updatedReviewDetails.setAmount(DataBuilderUtil.createScaledBigDecimal(250.00));

        UpdateHardshipReview body = UpdateHardshipReview.builder()
                .id(existingHardshipReview.getId())
                .nworCode(existingHardshipReview.getNewWorkReason().getCode())
                .cmuId(existingFinancialAssessment.getCmuId())
                .reviewResult("FAIL")
                .reviewDate(LocalDateTime.now())
                .resultDate(LocalDateTime.now())
                .solicitorCosts(SolicitorCosts.builder()
                        .solicitorRate(DataBuilderUtil.createScaledBigDecimal(1.23))
                        .solicitorHours(DataBuilderUtil.createScaledBigDecimal(12.00))
                        .solicitorVat(DataBuilderUtil.createScaledBigDecimal(123.45))
                        .solicitorDisb(DataBuilderUtil.createScaledBigDecimal(0.00))
                        .solicitorEstTotalCost(DataBuilderUtil.createScaledBigDecimal(345.67)).build())
                .disposableIncome(DataBuilderUtil.createScaledBigDecimal(13000.00))
                .disposableIncomeAfterHardship(DataBuilderUtil.createScaledBigDecimal(3000.00))
                .status(HardshipReviewStatus.COMPLETE)
                .reviewDetails(List.of(updatedReviewDetails))
                .updated(existingHardshipReview.getUpdated())
                .build();

        HardshipReviewDTO expectedResponse = HardshipReviewDTO.builder()
                .id(body.getId())
                .cmuId(body.getCmuId())
                .newWorkReason(NewWorkReason.builder().code(body.getNworCode()).build())
                .reviewDate(body.getReviewDate())
                .reviewResult(body.getReviewResult())
                .financialAssessmentId(existingHardshipReview.getFinancialAssessmentId())
                .resultDate(body.getResultDate())
                .userCreated(existingHardshipReview.getUserCreated())
                .courtType(existingHardshipReview.getCourtType())
                .reviewDetails(body.getReviewDetails())
                .solicitorCosts(body.getSolicitorCosts())
                .disposableIncome(body.getDisposableIncome())
                .disposableIncomeAfterHardship(body.getDisposableIncomeAfterHardship())
                .status(body.getStatus())
                .repId(existingHardshipReview.getRepId())
                .build();

        MvcResult result =
                runSuccessScenario(put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        HardshipReviewEntity updatedHardshipReview = hardshipReviewRepository.findByRepId(existingHardshipReview.getRepId());

        assertPersistedHardshipReviewDataIsCorrectOnUpdate(body, updatedHardshipReview);

        // Align date/id values.
        expectedResponse.setUpdated(updatedHardshipReview.getUpdated());

        List<HardshipReviewDetailEntity> reviewDetailEntities = updatedHardshipReview.getReviewDetails();

        for (int i = 0; i < reviewDetailEntities.size(); i++) {
            expectedResponse.getReviewDetails().get(i).setDateModified(reviewDetailEntities.get(i).getDateModified());
        }

        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
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
        assertThat(hardshipReviewEntity.getStatus()).isEqualTo(inputReviewData.getStatus());
        assertThat(hardshipReviewEntity.getSolicitorDisb()).isEqualTo(inputReviewData.getSolicitorCosts().getSolicitorDisb());
        assertThat(hardshipReviewEntity.getSolicitorEstTotalCost()).isEqualTo(inputReviewData.getSolicitorCosts().getSolicitorEstTotalCost());
        assertThat(hardshipReviewEntity.getSolicitorHours()).isEqualTo(inputReviewData.getSolicitorCosts().getSolicitorHours());
        assertThat(hardshipReviewEntity.getSolicitorRate()).isEqualTo(inputReviewData.getSolicitorCosts().getSolicitorRate());
        assertThat(hardshipReviewEntity.getSolicitorVat()).isEqualTo(inputReviewData.getSolicitorCosts().getSolicitorVat());
        assertThat(hardshipReviewEntity.getReviewResult()).isEqualTo(inputReviewData.getReviewResult());
        assertThat(hardshipReviewEntity.getDisposableIncomeAfterHardship()).isEqualTo(inputReviewData.getDisposableIncomeAfterHardship());
        assertThat(hardshipReviewEntity.getDisposableIncome()).isEqualTo(inputReviewData.getDisposableIncome());


        List<HardshipReviewDetailEntity> reviewDetails = hardshipReviewDetailRepository.findAllByHardshipReviewId(hardshipReviewEntity.getId());
        assertThat(reviewDetails.size()).isEqualTo(inputReviewData.getReviewDetails().size());

        for (int i = 0; i < reviewDetails.size(); i++) {
            HardshipReviewDetail expectedReviewDetails = inputReviewData.getReviewDetails().get(i);
            HardshipReviewDetailEntity persistedReviewDetails = reviewDetails.get(i);

            assertThat(persistedReviewDetails.getDetailType()).isEqualTo(expectedReviewDetails.getDetailType());
            assertThat(persistedReviewDetails.getUserCreated()).isEqualTo(expectedReviewDetails.getUserCreated());
            assertThat(persistedReviewDetails.getFrequency()).isEqualTo(expectedReviewDetails.getFrequency());
            assertThat(persistedReviewDetails.getOtherDescription()).isEqualTo(expectedReviewDetails.getOtherDescription());
            assertThat(persistedReviewDetails.getAmount()).isEqualTo(expectedReviewDetails.getAmount());
            assertThat(persistedReviewDetails.getAccepted()).isEqualTo(expectedReviewDetails.getAccepted());
            assertThat(persistedReviewDetails.getActive()).isEqualTo(expectedReviewDetails.getActive() ? YES : NO);
            assertThat(persistedReviewDetails.getDetailReason().getId().toString()).isEqualTo(expectedReviewDetails.getDetailReason().getId());
        }
    }

    private void runCreateHardshipReviewErrorScenario(String errorMessage, CreateHardshipReview body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private void runUpdateHardshipReviewErrorScenario(String errorMessage, UpdateHardshipReview body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }


    private void setupTestData() {
        existingNewWorkReason = mockNewWorkReasonRepository.save(
                TestEntityDataBuilder.getNewWorkReasonEntity());
        existingHardshipReviewDetailReason = hardshipReviewDetailReasonRepository.save(
                HardshipReviewDetailReasonEntity.builder()
                    .id(1)
                    .reason("test-reason")
                    .detailType(HardshipReviewDetailType.EXPENDITURE)
                    .userCreated(TEST_USER).build());
        existingFinancialAssessment = financialAssessmentRepository.save(getTestFinancialAssessment(4444));
        existingUnlinkedFinancialAssessment = financialAssessmentRepository.save(getTestFinancialAssessment(5555));
        existingHardshipReview = hardshipReviewRepository.save(
                getTestHardshipReview(existingFinancialAssessment.getRepId(), existingFinancialAssessment.getId()));

    }

    private HardshipReviewEntity getTestHardshipReview(Integer repId, Integer supportingAssessmentId) {
        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReview.addReviewDetail(getTestHardshipReviewDetailEntity());
        hardshipReview.setFinancialAssessmentId(supportingAssessmentId);
        hardshipReview.setRepId(repId);
        hardshipReview.setStatus(HardshipReviewStatus.IN_PROGRESS);
        return hardshipReview;
    }

    private HardshipReviewDetailEntity getTestHardshipReviewDetailEntity() {
        HardshipReviewDetailEntity hardshipReviewDetail = TestEntityDataBuilder.getHardshipReviewDetailsEntity();
        hardshipReviewDetail.setId(null);
        hardshipReviewDetail.setDetailReason(existingHardshipReviewDetailReason);
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
                        existingReviewDetails.getId(), existingReviewDetails.getDateModified(), getTestHardshipReviewDetailReason())));
        return hardshipReviewDTO;
    }

    private HardshipReviewDetail getTestHardshipReviewDetail(Integer id, LocalDateTime dateModified, HardshipReviewDetailReason detailReason) {
        HardshipReviewDetail reviewDetail = TestModelDataBuilder.getHardshipReviewDetail();

        reviewDetail.setId(id);
        reviewDetail.setDateModified(dateModified);

        if (detailReason != null) {
            reviewDetail.setDetailReason(detailReason);
        }

        return reviewDetail;
    }

    private HardshipReviewDetailReason getTestHardshipReviewDetailReason() {
        return HardshipReviewDetailReason.builder()
                .id(existingHardshipReviewDetailReason.getId().toString())
                .reason(existingHardshipReviewDetailReason.getReason())
                .userCreated(existingHardshipReviewDetailReason.getUserCreated())
                .dateCreated(existingHardshipReviewDetailReason.getDateCreated())
                .detailType(existingHardshipReviewDetailReason.getDetailType())
                .build();
    }

    private FinancialAssessmentEntity getTestFinancialAssessment(Integer repId) {
        return FinancialAssessmentEntity.builder()
                .repId(repId)
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
