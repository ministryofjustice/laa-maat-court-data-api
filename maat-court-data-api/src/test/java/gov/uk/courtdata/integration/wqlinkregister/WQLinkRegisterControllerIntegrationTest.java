package gov.uk.courtdata.integration.wqlinkregister;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class WQLinkRegisterControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-link-register";
    private static final Integer INVALID_REP_ID = 12312334;

    @BeforeEach
    void setUp() {
        repos.wqLinkRegister.save(TestEntityDataBuilder.getWQLinkRegisterEntity(8064716));
    }

    @Test
    void givenAInvalidMaatId_whenFindOffenceByMaatIdIsInvoked_thenEmptyRecordsIsReturned() throws Exception {
        assertTrue(runSuccessScenario(List.of(), get(ENDPOINT_URL + "/" + INVALID_REP_ID)));
    }

    @Test
    void givenAValidMaatId_whenFindOffenceByMaatIdIsInvoked_thenWQLinkRegisterIsReturned() throws Exception {
        List wqLinkRegisterEntityList = List.of(TestEntityDataBuilder.getWQLinkRegisterEntity(8064716));
        assertTrue(runSuccessScenario(wqLinkRegisterEntityList, get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)));
    }
}
