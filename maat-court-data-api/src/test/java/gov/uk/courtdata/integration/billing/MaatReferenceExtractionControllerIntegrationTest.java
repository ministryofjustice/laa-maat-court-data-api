package gov.uk.courtdata.integration.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class MaatReferenceExtractionControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/maat-references";

    @Test
    void givenAValidRepOrder_whenPopulateMaatReferencesToExtract_thenSuccessResponseIsReturned() throws Exception {
        repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrderToSendToCclf());

        assertThat(runSuccessScenario(post(ENDPOINT_URL)).getResponse().getStatus())
                .isEqualTo(200);
        assertThat(repos.maatReference.count()).isOne();
    }

    @Test
    void givenRecordsAlreadyExist_whenPopulateMaatReferencesToExtract_thenErrorResponseIsReturned() throws Exception {
        repos.maatReference.saveAndFlush(TestEntityDataBuilder.getMaatReferenceEntity());

        assertThat(runServerErrorScenario("The MAAT_REFS_TO_EXTRACT table already has entries", post(ENDPOINT_URL)))
                .isTrue();
    }

    @Test
    void givenRecordsExist_whenDeleteMaatReferences_thenSuccessResponseIsReturned() throws Exception {
        repos.maatReference.saveAndFlush(TestEntityDataBuilder.getMaatReferenceEntity());

        assertThat(repos.maatReference.count()).isOne();
        assertThat(runSuccessScenario(delete(ENDPOINT_URL)).getResponse().getStatus())
                .isEqualTo(200);
        assertThat(repos.maatReference.count()).isZero();
    }
}
