package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class RepOrderControllerIntegrationTest extends MockMvcIntegrationTest {

    public static final Integer INVALID_REP_ID = 9999;
    public static final Integer INVALID_MVO_ID = 8888;
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

    @BeforeAll
    static void setUp(@Autowired RepOrderRepository repOrderRepository, @Autowired RepOrderMvoRepository repOrderMvoRepository, @Autowired RepOrderMvoRegRepository repOrderMvoRegRepository) {
        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
        repOrderMvoRepository.save(TestEntityDataBuilder.getRepOrderMvoEntity(TestEntityDataBuilder.MVO_ID));
        repOrderMvoRegRepository.save(TestEntityDataBuilder.getRepOrderMvoRegEntity(TestEntityDataBuilder.REP_ID));
    }

    @AfterAll
    static void cleanUp(@Autowired RepOrderRepository repOrderRepository, @Autowired FinancialAssessmentRepository financialAssessmentRepository, @Autowired PassportAssessmentRepository passportAssessmentRepository, @Autowired RepOrderMvoRepository repOrderMvoRepository, @Autowired RepOrderMvoRegRepository repOrderMvoRegRepository) {
        financialAssessmentRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        repOrderMvoRegRepository.deleteAll();
        repOrderMvoRepository.deleteAll();
        repOrderRepository.deleteAll();
    }

    @Test
    public void givenInvalidRepId_whenGetRepOrderIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        runNotFoundErrorScenario("No Rep Order found for ID: " + INVALID_REP_ID, get(BASE_URL + INVALID_REP_ID));
    }

    @Test
    public void givenValidRepId_whenGetRepOrderIsInvoked_thenRepOrderIsReturned() throws Exception {
        runSuccessScenario(TestModelDataBuilder.getRepOrderDTO(), get(BASE_URL + TestEntityDataBuilder.REP_ID));
    }

    @Test
    public void givenRepIdIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("Rep Id is missing from request and is required", post(BASE_URL + "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UpdateAppDateCompleted.builder().build())));
    }

    @Test
    public void givenDateIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("Assessment Date completed is missing from request and is required", post(BASE_URL + "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UpdateAppDateCompleted.builder().repId(TestModelDataBuilder.REP_ID).build())));
    }

    @Test
    public void givenInvalidRepId_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("MAAT/REP ID: " + INVALID_REP_ID + " is invalid.", post(BASE_URL + "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(UpdateAppDateCompleted.builder().repId(INVALID_REP_ID).assessmentDateCompleted(LocalDateTime.now()).build())));
    }

    @Test
    public void givenValidParameters_whenUpdateAppDateCompletedIsInvoked_theCompletedDateShouldUpdate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter);

        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + "/update-date-completed").contentType(MediaType.APPLICATION_JSON).content(TestModelDataBuilder.getUpdateAppDateCompletedJson()).contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repOrderRepository.getById(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getId()).isEqualTo(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getAssessmentDateCompleted()).isEqualTo(expectedDate);
    }

    @Test
    public void givenInvalidMvoId_whenGetDateDeletedIsNullFromRepOrderMvoRegIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        runNotFoundErrorScenario("No Rep Order MVO Reg found for ID: " + INVALID_MVO_ID, get(MVO_REG_ENDPOINT_URL + "/" + INVALID_MVO_ID + "/" + CURRENT_REGISTRATION));
    }

    @Test
    public void givenValidMvoId_whenGetDateDeletedIsNullFromRepOrderMvoRegIsInvoked_thenRepOrderMvoRegIsReturned() throws Exception {
        runSuccessScenario(List.of(TestModelDataBuilder.getRepOrderMvoRegDTO()), get(MVO_REG_ENDPOINT_URL + "/" + TestEntityDataBuilder.MVO_ID + "/" + CURRENT_REGISTRATION));
    }

    @Test
    public void givenInvalidRepId_whenGetRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        runNotFoundErrorScenario("No Rep Order MVO found for ID: " + INVALID_REP_ID, get(MVO_ENDPOINT_URL + "/" + INVALID_REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES));
    }

    @Test
    public void givenValidRepId_whenGetRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoIsReturned() throws Exception {
        runSuccessScenario(TestModelDataBuilder.getRepOrderMvoDTO(), get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES));
    }

}
