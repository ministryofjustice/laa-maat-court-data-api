package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class CCOutcomeControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/rep-orders/cc-outcome";

    private static final Integer INVALID_REP_ID = -1;
    private static final Integer INVALID_OUTCOME_ID = -1;
    @Autowired
    private CrownCourtProcessingRepository courtProcessingRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    private RepOrderEntity existingRepOrder;
    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        existingRepOrder = repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
    }

    @AfterEach
    void clearUp() {
        existingRepOrder = null;
        courtProcessingRepository.deleteAll();
    }

    @Test
    void givenAInvalidData_whenCreateIsInvoked_thenErrorIsThrown() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setUserCreated(null);
        repOrderCCOutCome.setId(null);
        assertTrue(runBadRequestErrorScenario("User created is required",
                post(endpointUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome))));
    }

    @Test
    void givenAValidData_whenCreateIsInvoked_thenCreateOutcomeIsSuccess() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(null);
        runSuccessScenario(post(endpointUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome)));
        List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities = courtProcessingRepository.findByRepId(TestModelDataBuilder.REP_ID);
        assertThat(repOrderCCOutComeEntities).isNotNull();
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = repOrderCCOutComeEntities.get(0);

        softly.assertThat(repOrderCCOutComeEntity.getRepId()).isEqualTo(repOrderCCOutCome.getRepId());
        softly.assertThat(repOrderCCOutComeEntity.getOutcome()).isEqualTo(repOrderCCOutCome.getOutcome());
        softly.assertThat(repOrderCCOutComeEntity.getCaseNumber()).isEqualTo(repOrderCCOutCome.getCaseNumber());
        softly.assertThat(repOrderCCOutComeEntity.getCrownCourtCode()).isEqualTo(repOrderCCOutCome.getCrownCourtCode());
        softly.assertThat(repOrderCCOutComeEntity.getDateCreated()).isNotNull();
        softly.assertThat(repOrderCCOutComeEntity.getUserCreated()).isEqualTo(repOrderCCOutCome.getUserCreated());
        softly.assertThat(repOrderCCOutComeEntity.getOutcomeDate()).isEqualTo(repOrderCCOutCome.getOutcomeDate());
    }

    @Test
    void givenAInvalidRepId_whenFindIsInvoked_thenReturnEmpty() throws Exception {
        List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities = courtProcessingRepository.findByRepId(INVALID_REP_ID);
        assertThat(repOrderCCOutComeEntities.isEmpty()).isTrue();
    }

    @Test
    void givenAInValidOutcomeId_whenUpdateIsInvoked_thenThrowException() throws Exception {
        assertTrue(runNotFoundErrorScenario("No CC Outcome found for ID: -1",
                MockMvcRequestBuilders.put(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutCome(INVALID_OUTCOME_ID))))
        );
    }

    @Test
    void givenAValidData_whenUpdateIsInvoked_thenShouldUpdatedCCOutCome() throws Exception {
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = courtProcessingRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderCCOutComeEntity());
        runSuccessScenario(MockMvcRequestBuilders.put(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutCome(1))));
        RepOrderCCOutComeEntity ccOutCome = courtProcessingRepository.findById(1).get();
        softly.assertThat(TestModelDataBuilder.REP_ID).isEqualTo(ccOutCome.getRepId());
        softly.assertThat(TestModelDataBuilder.TEST_CASE_ID.toString()).isEqualTo(ccOutCome.getCaseNumber());
        softly.assertThat("430").isEqualTo(ccOutCome.getCrownCourtCode());
        softly.assertThat(TestModelDataBuilder.TEST_USER).isEqualTo(ccOutCome.getUserModified());
        softly.assertThat(ccOutCome.getDateModified()).isNotNull();
    }

}
