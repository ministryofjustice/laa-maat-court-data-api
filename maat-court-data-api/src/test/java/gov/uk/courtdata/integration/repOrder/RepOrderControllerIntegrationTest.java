package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
class RepOrderControllerIntegrationTest extends MockMvcIntegrationTest {

    public static final Integer INVALID_REP_ID = 9999;
    public static final Integer INVALID_MVO_ID = 8888;
    public static final Integer REP_ORDER_ID_NO_SENTENCE_ORDER_DATE = 4321;
    public static final String BASE_URL = "/api/internal/v1/assessment/rep-orders/";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";
    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";
    private static final String CURRENT_REGISTRATION = "current-registration";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private RepOrderMvoRepository repOrderMvoRepository;

    @Autowired
    private RepOrderMvoRegRepository repOrderMvoRegRepository;

    @InjectSoftAssertions
    private SoftAssertions softly;


    @BeforeEach
    void setUp(@Autowired RepOrderRepository repOrderRepository,
               @Autowired RepOrderMvoRepository repOrderMvoRepository,
               @Autowired RepOrderMvoRegRepository repOrderMvoRegRepository) {


        repOrderRepository.save(
                RepOrderEntity.builder().id(REP_ORDER_ID_NO_SENTENCE_ORDER_DATE).build()
        );
        repOrderRepository.save(
                TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID)
        );
        repOrderMvoRepository.save(
                TestEntityDataBuilder.getRepOrderMvoEntity(TestEntityDataBuilder.MVO_ID)
        );
        repOrderMvoRegRepository.save(
                TestEntityDataBuilder.getRepOrderMvoRegEntity(TestEntityDataBuilder.REP_ID)
        );
    }

    @AfterEach
    void cleanUp(@Autowired RepOrderRepository repOrderRepository,
                 @Autowired FinancialAssessmentRepository financialAssessmentRepository,
                 @Autowired PassportAssessmentRepository passportAssessmentRepository,
                 @Autowired RepOrderMvoRepository repOrderMvoRepository,
                 @Autowired RepOrderMvoRegRepository repOrderMvoRegRepository) {

        financialAssessmentRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        repOrderMvoRegRepository.deleteAll();
        repOrderMvoRepository.deleteAll();
        repOrderRepository.deleteAll();
    }

    private RepOrderDTO getUpdatedRepOrderDTO() {
        RepOrderEntity repOrderEntity = repOrderRepository.getReferenceById(TestModelDataBuilder.REP_ID);
        RepOrderDTO repOrderDTO = TestModelDataBuilder.getRepOrderDTO();
        repOrderDTO.setDateModified(repOrderEntity.getDateModified());
        repOrderDTO.setSentenceOrderDate(repOrderEntity.getSentenceOrderDate());
        return repOrderDTO;
    }

    @Test
    void givenInvalidRepId_whenFindInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order found for ID: " + INVALID_REP_ID,
                get(BASE_URL + INVALID_REP_ID)));
    }

    @Test
    void givenIncorrectRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No Rep Order found for ID: " + REP_ORDER_ID_NO_SENTENCE_ORDER_DATE,
                get(BASE_URL + REP_ORDER_ID_NO_SENTENCE_ORDER_DATE + "?has_sentence_order_date=true")
        ));
    }

    @Test
    void givenValidRepId_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        assertTrue(runSuccessScenario(repOrderDTO, get(BASE_URL + TestEntityDataBuilder.REP_ID)));
    }

    @Test
    void givenValidRepIdAndSentenceOrderDateFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() throws Exception {
        RepOrderDTO repOrderDTO = getUpdatedRepOrderDTO();
        assertTrue(runSuccessScenario(repOrderDTO,
                get(BASE_URL + TestEntityDataBuilder.REP_ID + "?has_sentence_order_date=true")
        ));
    }

    @Test
    void givenRepIdIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Rep Id is missing from request and is required",
                post(BASE_URL + "update-date-completed")
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
                post(BASE_URL + "update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                        UpdateAppDateCompleted.builder()
                                                .repId(TestModelDataBuilder.REP_ID)
                                                .build()
                                )
                        )
        ));
    }

    @Test
    void givenInvalidRepId_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("MAAT/REP ID: " + INVALID_REP_ID + " is invalid.",
                post(BASE_URL + "update-date-completed")
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

        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + "update-date-completed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson())
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repOrderRepository.getReferenceById(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getId()).isEqualTo(TestModelDataBuilder.REP_ID);
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
        assertTrue(runSuccessScenario(TestModelDataBuilder.getRepOrderMvoDTO(),
                get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES)
        ));
    }

    @Test
    void givenRepIdIsMissing_whenUpdateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario(
                "MAAT ID is required.",
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
                "MAAT/REP ID: " + INVALID_REP_ID + " is invalid.",
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

        UpdateRepOrder request = TestModelDataBuilder.getUpdateRepOrder();

        runSuccessScenario(MockMvcRequestBuilders.put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repOrderRepository.getReferenceById(TestModelDataBuilder.REP_ID);

        softly.assertThat(repOrderEntity.getCaseId())
                .isEqualTo(request.getCaseId());

        softly.assertThat(repOrderEntity.getCatyCaseType())
                .isEqualTo(request.getCatyCaseType());

        softly.assertThat(repOrderEntity.getAppealTypeCode())
                .isEqualTo(request.getAppealTypeCode());

        softly.assertThat(repOrderEntity.getArrestSummonsNo())
                .isEqualTo(request.getArrestSummonsNo());

        softly.assertThat(repOrderEntity.getUserModified())
                .isEqualTo(TestModelDataBuilder.TEST_USER);

        softly.assertThat(repOrderEntity.getMagsOutcome())
                .isEqualTo(request.getMagsOutcome());

        softly.assertThat(repOrderEntity.getMagsOutcomeDate())
                .isEqualTo(request.getMagsOutcomeDate());

        softly.assertThat(repOrderEntity.getMagsOutcomeDateSet())
                .isEqualTo(request.getMagsOutcomeDateSet());

        softly.assertThat(repOrderEntity.getCommittalDate())
                .isEqualTo(request.getCommittalDate());

        softly.assertThat(repOrderEntity.getDecisionReasonCode())
                .isEqualTo(request.getDecisionReasonCode());

        softly.assertThat(repOrderEntity.getCrownRepId())
                .isEqualTo(request.getCrownRepId());

        softly.assertThat(repOrderEntity.getCrownRepOrderDecision())
                .isEqualTo(request.getCrownRepOrderDecision());

        softly.assertThat(repOrderEntity.getCrownRepOrderType())
                .isEqualTo(request.getCrownRepOrderType());

        softly.assertThat(repOrderEntity.getCrownRepOrderDate())
                .isEqualTo(request.getCrownRepOrderDate());

        softly.assertThat(repOrderEntity.getCrownWithdrawalDate())
                .isEqualTo(request.getCrownWithdrawalDate());

        softly.assertThat(repOrderEntity.getIsImprisoned())
                .isEqualTo(request.getIsImprisoned());

        softly.assertThat(repOrderEntity.getAssessmentDateCompleted())
                .isEqualTo(request.getAssessmentDateCompleted());

        softly.assertThat(repOrderEntity.getSentenceOrderDate())
                .isEqualTo(request.getSentenceOrderDate());

        softly.assertThat(repOrderEntity.getApplicantHistoryId())
                .isEqualTo(request.getApplicantHistoryId());

        softly.assertThat(repOrderEntity.getEvidenceFeeLevel())
                .isEqualTo(request.getEvidenceFeeLevel());

        softly.assertThat(repOrderEntity.getBankAccountNo())
                .isEqualTo(request.getBankAccountNo());

        softly.assertThat(repOrderEntity.getBankAccountName())
                .isEqualTo(request.getBankAccountName());

        softly.assertThat(repOrderEntity.getPaymentMethod())
                .isEqualTo(request.getPaymentMethod());

        softly.assertThat(repOrderEntity.getPreferredPaymentDay())
                .isEqualTo(request.getPreferredPaymentDay());

        softly.assertThat(repOrderEntity.getSortCode())
                .isEqualTo(request.getSortCode());

        softly.assertThat(repOrderEntity.getIsSendToCCLF())
                .isEqualTo(request.getIsSendToCCLF());

        softly.assertThat(repOrderEntity.getAreaId())
                .isEqualTo(request.getAreaId());

        softly.assertThat(repOrderEntity.getCmuId())
                .isEqualTo(request.getCmuId());

        softly.assertThat(repOrderEntity.getIsCaseTransferred())
                .isEqualTo(request.getIsCaseTransferred());

        softly.assertThat(repOrderEntity.getIsBenchWarrantIssued())
                .isEqualTo(request.getIsBenchWarrantIssued());

        softly.assertThat(repOrderEntity.getAppealSentenceOrderChangedDate())
                .isEqualTo(request.getAppealSentenceOrderChangedDate());

        softly.assertThat(repOrderEntity.getAppealSentenceOrderDate())
                .isEqualTo(request.getAppealSentenceOrderDate());

        softly.assertThat(repOrderEntity.getAppealReceivedDate())
                .isEqualTo(request.getAppealReceivedDate());

        softly.assertThat(repOrderEntity.getAppealTypeDate())
                .isEqualTo(request.getAppealTypeDate());

        softly.assertThat(repOrderEntity.getFirstCapitalReminderDate())
                .isEqualTo(request.getFirstCapitalReminderDate());

        softly.assertThat(repOrderEntity.getAllCapitalEvidenceReceivedDate())
                .isEqualTo(request.getAllCapitalEvidenceReceivedDate());

        softly.assertThat(repOrderEntity.getApplicationId())
                .isEqualTo(request.getApplicationId());

        softly.assertThat(repOrderEntity.getCapitalAllowanceReinstatedDate())
                .isEqualTo(request.getCapitalAllowanceReinstatedDate());

        softly.assertThat(repOrderEntity.getCapitalAllowanceWithheldDate())
                .isEqualTo(request.getCapitalAllowanceWithheldDate());

        softly.assertThat(repOrderEntity.getCapitalEvidenceDueDate())
                .isEqualTo(request.getCapitalEvidenceDueDate());

        softly.assertThat(repOrderEntity.getCapitalNote())
                .isEqualTo(request.getCapitalNote());

        softly.assertThat(repOrderEntity.getCapitalAllowance())
                .isEqualTo(request.getCapitalAllowance());

        softly.assertThat(repOrderEntity.getIsCourtCustody())
                .isEqualTo(request.getIsCourtCustody());

        softly.assertThat(repOrderEntity.getDateReceived())
                .isEqualTo(request.getDateReceived());

        softly.assertThat(repOrderEntity.getDateStatusDue())
                .isEqualTo(request.getDateStatusDue());

        softly.assertThat(repOrderEntity.getDateStatusSet())
                .isEqualTo(request.getDateStatusSet());

        softly.assertThat(repOrderEntity.getDecisionDate())
                .isEqualTo(request.getDecisionDate());

        softly.assertThat(repOrderEntity.getIojResultNote())
                .isEqualTo(request.getIojResultNote());

        softly.assertThat(repOrderEntity.getMacoCourt())
                .isEqualTo(request.getMacoCourt());

        softly.assertThat(repOrderEntity.getMagsWithdrawalDate())
                .isEqualTo(request.getMagsWithdrawalDate());

        softly.assertThat(repOrderEntity.getIsNoCapitalDeclared())
                .isEqualTo(request.getIsNoCapitalDeclared());

        softly.assertThat(repOrderEntity.getOftyOffenceType())
                .isEqualTo(request.getOftyOffenceType());

        softly.assertThat(repOrderEntity.getUseSuppAddressForPost())
                .isEqualTo(request.getUseSuppAddressForPost());

        softly.assertThat(repOrderEntity.getPostalAddressId())
                .isEqualTo(request.getPostalAddressId());

        softly.assertThat(repOrderEntity.getRorsStatus())
                .isEqualTo(request.getRorsStatus());

        softly.assertThat(repOrderEntity.getStatusReason())
                .isEqualTo(request.getStatusReason());

        softly.assertThat(repOrderEntity.getSuppAccountCode())
                .isEqualTo(request.getSuppAccountCode());

        softly.assertThat(repOrderEntity.getIsWelshCorrespondence())
                .isEqualTo(request.getIsWelshCorrespondence());

        softly.assertThat(repOrderEntity.getCinrCode())
                .isEqualTo(request.getCinrCode());

        softly.assertThat(repOrderEntity.getIsPartner())
                .isEqualTo(request.getIsPartner());

        softly.assertThat(repOrderEntity.getIsRetrial())
                .isEqualTo(request.getIsRetrial());

        softly.assertThat(repOrderEntity.getEfmDateStamp())
                .isEqualTo(request.getEfmDateStamp());

        softly.assertThat(repOrderEntity.getSolicitorName())
                .isEqualTo(request.getSolicitorName());

        softly.assertThat(repOrderEntity.getHearingDate())
                .isEqualTo(request.getHearingDate());

    }

    @Test
    void givenHeadRequestWithRepId_whenFindIsInvoked_thenReturnMetadata() throws Exception {
        var response = runSuccessScenario(
                head(BASE_URL + TestModelDataBuilder.REP_ID)
        );
        var content = response.getResponse().getContentAsString();

        softly.assertThat(content).isEqualTo("");
        softly.assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");

        response = runSuccessScenario(
                head(BASE_URL + INVALID_REP_ID)
        );
        content = response.getResponse().getContentAsString();

        softly.assertThat(content).isEqualTo("");
        softly.assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("0");
    }
}
