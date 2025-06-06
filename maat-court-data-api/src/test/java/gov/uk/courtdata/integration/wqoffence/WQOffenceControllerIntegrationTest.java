package gov.uk.courtdata.integration.wqoffence;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class WQOffenceControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_CASE_ID = 775314;
    private static final String INVALID_OFFENCE_ID = "754169aa-26895b-4bb095-a7kkb0";
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-offence";

    @BeforeEach
    void setUp() {
        repos.wqOffence.save(TestEntityDataBuilder.getWQOffenceEntity(8064716));
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
