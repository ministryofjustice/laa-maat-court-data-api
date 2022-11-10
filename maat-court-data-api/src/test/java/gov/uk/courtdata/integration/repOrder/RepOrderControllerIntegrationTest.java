package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class RepOrderControllerIntegrationTest extends MockMvcIntegrationTest {

    @Autowired
    private RepOrderRepository repOrderRepository;

    public static final Integer INVALID_REP_ID = 2345;
    public static final String BASE_URL = "/api/internal/v1/assessment/rep-orders/";

    @BeforeEach
    void setUp(@Autowired RepOrderRepository repOrderRepository) {
        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
    }

    @AfterEach
    void cleanUp(@Autowired RepOrderRepository repOrderRepository,
                        @Autowired FinancialAssessmentRepository financialAssessmentRepository,
                        @Autowired PassportAssessmentRepository passportAssessmentRepository) {
        financialAssessmentRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        repOrderRepository.deleteAll();
    }

    @Test
    public void givenInvalidRepId_whenGetRepOrderIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        runNotFoundErrorScenario(
                "No Rep Order found for ID: " + INVALID_REP_ID,
                get(BASE_URL + INVALID_REP_ID)
        );
    }

    @Test
    public void givenValidRepId_whenGetRepOrderIsInvoked_thenRepOrderIsReturned() throws Exception {
        runSuccessScenario(
                TestModelDataBuilder.getRepOrderDTO(),
                get(BASE_URL + TestEntityDataBuilder.REP_ID)
        );
    }

    @Test
    public void givenRepIdIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(
                "Rep Id is missing from request and is required",
                post(BASE_URL + "/update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateAppDateCompleted.builder()
                                        .build())
                        )
        );
    }

    @Test
    public void givenDateIsMissing_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(
                "Assessment Date completed is missing from request and is required",
                post(BASE_URL + "/update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateAppDateCompleted.builder()
                                        .repId(TestModelDataBuilder.REP_ID)
                                        .build())
                        )
        );
    }

    @Test
    public void givenInvalidRepId_whenUpdateAppDateCompletedIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(
                "MAAT/REP ID: " + INVALID_REP_ID + " is invalid.",
                post(BASE_URL + "/update-date-completed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateAppDateCompleted.builder()
                                        .repId(INVALID_REP_ID)
                                        .assessmentDateCompleted(LocalDateTime.now())
                                        .build())
                        )
        );
    }

    @Test
    public void givenValidParameters_whenUpdateAppDateCompletedIsInvoked_theCompletedDateShouldUpdate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter);

        runSuccessScenario(MockMvcRequestBuilders.post(BASE_URL + "/update-date-completed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson())
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repOrderRepository.getById(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getId()).isEqualTo(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getAssessmentDateCompleted()).isEqualTo(expectedDate);
    }

    @Test
    public void givenRepIdIsMissing_whenUpdateRepOrderIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(
                "Rep Id is missing from request and is required",
                put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateRepOrder.builder()
                                        .build())
                        )
        );
    }

    @Test
    public void givenInvalidRepId_whenUpdateRepOrderIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(
                "MAAT/REP ID: " + INVALID_REP_ID + " is invalid.",
                put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdateRepOrder.builder()
                                        .repId(INVALID_REP_ID)
                                        .build())
                        )
        );
    }


    @Test
    public void givenValidParameters_whenUpdateRepOrderIsInvoked_theCompletedDateShouldUpdate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime expectedDate = LocalDateTime.parse(TestModelDataBuilder.APP_DATE_COMPLETED, formatter);

        runSuccessScenario(MockMvcRequestBuilders.put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestModelDataBuilder.getUpdateRepOrderJson())
                .contentType(MediaType.APPLICATION_JSON));

        RepOrderEntity repOrderEntity = repOrderRepository.getById(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getId()).isEqualTo(TestModelDataBuilder.REP_ID);
        assertThat(repOrderEntity.getSentenceOrderDate()).isEqualTo(expectedDate);
        assertThat(repOrderEntity.getUserModified()).isEqualTo(TestModelDataBuilder.TEST_USER);
    }
}
