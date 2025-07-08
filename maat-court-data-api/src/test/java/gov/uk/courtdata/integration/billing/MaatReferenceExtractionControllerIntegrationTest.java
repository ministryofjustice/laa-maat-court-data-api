package gov.uk.courtdata.integration.billing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class MaatReferenceExtractionControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/maat-references";

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;
    
    @Test
    void givenAValidRepOrder_whenPopulateMaatReferencesToExtract_thenSuccessResponseIsReturned() throws Exception {
        repos.repOrder.saveAndFlush(testEntityDataBuilder.getPopulatedRepOrderToSendToCclf());
        
        assertThat(runSuccessScenario(post(ENDPOINT_URL)).getResponse().getStatus()).isEqualTo(200);
        assertEquals(1, repos.maatReference.count());
    }
    
    @Test
    void givenRecordsAlreadyExist_whenPopulateMaatReferencesToExtract_thenReturnError() throws Exception {
        repos.maatReference.saveAndFlush(testEntityDataBuilder.getMaatReferenceEntity());
        
        assertTrue(
            runServerErrorScenario("The MAAT_REFS_TO_EXTRACT table already has entries",
                post(ENDPOINT_URL)));
    }

    @Test
    void givenRecordsExist_whenDeleteMaatReferences_thenSuccessResponseIsReturned() throws Exception {
        repos.maatReference.saveAndFlush(testEntityDataBuilder.getMaatReferenceEntity());

        assertEquals(1, repos.maatReference.count());
        assertThat(runSuccessScenario(delete(ENDPOINT_URL)).getResponse().getStatus()).isEqualTo(200);
        assertEquals(0, repos.maatReference.count());
    }

}
