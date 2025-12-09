package gov.uk.courtdata.integration.offence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.OffenceDTO;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class OffenceControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_CASE_ID = 665314;
    private static final String INVALID_OFFENCE_ID = "634169aa-265b-4bb5-a7b0";
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/offence";

    @BeforeEach
    void setUp() {
        repos.offence.save(TestEntityDataBuilder.getOffenceEntity(8064716));
    }

    @Test
    void givenAInvalidCaseId_whenFindOffenceByCaseIdIsInvoked_thenEmptyIsReturned()
            throws Exception {
        assertTrue(runSuccessScenario(List.of(), get(ENDPOINT_URL + "/case/" + INVALID_CASE_ID)));
    }

    @Test
    void givenAValidCaseId_whenFindOffenceByCaseIdIsInvoked_thenOffenceIsReturned()
            throws Exception {
        List<OffenceDTO> offenceDTOList =
                List.of(TestModelDataBuilder.getOffenceDTO(8064716));
        assertTrue(runSuccessScenario(offenceDTOList,
                get(ENDPOINT_URL + "/case/" + TestEntityDataBuilder.TEST_CASE_ID)));
    }

    @Test
    void givenAInvalidCaseIdAndOffenceId_whenGetNewOffenceCountIsInvoked_thenZeroIsReturned()
            throws Exception {
        var response = runSuccessScenario(
                get(ENDPOINT_URL + "/" + INVALID_OFFENCE_ID + "/case/" + INVALID_CASE_ID));
        assertThat(response.getResponse().getContentAsString()).isEqualTo("0");
    }

    @Test
    void givenAValidCaseIdAndOffenceId_whenGetNewOffenceCountIsInvoked_thenOneIsReturned()
            throws Exception {
        var response = runSuccessScenario(
                get(ENDPOINT_URL + "/" + TestEntityDataBuilder.TEST_OFFENCE_ID + "/case/"
                        + TestEntityDataBuilder.TEST_CASE_ID));
        assertThat(response.getResponse().getContentAsString()).isEqualTo("1");
    }
}
