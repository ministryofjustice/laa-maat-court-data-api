package gov.uk.courtdata.integration.ccoutcome;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
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
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class CCOutComeControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/cc-outcome";

    private static final Integer INVALID_REP_ID = -1;

    @Autowired
    private CrownCourtProcessingRepository courtProcessingRepository;

    @Autowired
    private RepOrderRepository repOrderRepository;

    private RepOrderEntity existingRepOrder;

    @BeforeEach
    public void setUp() {
        existingRepOrder = repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
    }

    @AfterEach
    public void clearUp() {
        existingRepOrder = null;
        courtProcessingRepository.deleteAll();
    }

    @Test
    public void givenAInvalidData_whenCreateCCOutComeIsInvoked_thenErrorIsThrown() throws Exception {
        RepOrderCCOutCome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setUserCreated(null);
        repOrderCCOutCome.setId(null);
        assertTrue(runBadRequestErrorScenario(String.format("User created is required"),
                post(endpointUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome))));
    }
    @Test
    public void givenAValidData_whenCreateCCOutComeIsInvoked_thenShouldCreatedCCOutCome() throws Exception {
        RepOrderCCOutCome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(null);
        runSuccessScenario(post(endpointUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome)));
        List<RepOrderCCOutComeEntity> repOrderCCOutComeEntities = courtProcessingRepository.findByRepId(TestModelDataBuilder.REP_ID);
        assertThat(repOrderCCOutComeEntities).isNotNull();
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = repOrderCCOutComeEntities.get(0);
        assertThat(repOrderCCOutComeEntity.getRepId()).isEqualTo(repOrderCCOutCome.getRepId());
        assertThat(repOrderCCOutComeEntity.getCcooOutcome()).isEqualTo(repOrderCCOutCome.getCcooOutcome());
        assertThat(repOrderCCOutComeEntity.getCaseNumber()).isEqualTo(repOrderCCOutCome.getCaseNumber());
        assertThat(repOrderCCOutComeEntity.getCrownCourtCode()).isEqualTo(repOrderCCOutCome.getCrownCourtCode());
        assertThat(repOrderCCOutComeEntity.getDateCreated()).isNotNull();
        assertThat(repOrderCCOutComeEntity.getUserCreated()).isEqualTo(repOrderCCOutCome.getUserCreated());
        assertThat(repOrderCCOutComeEntity.getCcooOutcomeDate()).isEqualTo(repOrderCCOutCome.getCcooOutcomeDate());
    }

    @Test
    public void givenAValidData_whenUpdateCCOutComeIsInvoked_thenShouldUpdatedCCOutCome() throws Exception {
         courtProcessingRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderCCOutComeEntity());
        runSuccessScenario(MockMvcRequestBuilders.put(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutCome(1))));
        RepOrderCCOutComeEntity ccOutCome = courtProcessingRepository.findById(1).get();
        assertThat(TestModelDataBuilder.REP_ID).isEqualTo(ccOutCome.getRepId());
        assertThat(TestModelDataBuilder.TEST_CASE_ID.toString()).isEqualTo(ccOutCome.getCaseNumber());
        assertThat("430").isEqualTo(ccOutCome.getCrownCourtCode());
        assertThat(TestModelDataBuilder.TEST_USER).isEqualTo(ccOutCome.getUserModified());
        assertThat(ccOutCome.getDateModified()).isNotNull();
    }

}
