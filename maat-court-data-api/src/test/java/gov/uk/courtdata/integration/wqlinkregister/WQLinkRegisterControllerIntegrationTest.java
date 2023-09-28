package gov.uk.courtdata.integration.wqlinkregister;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import gov.uk.courtdata.util.RepositoryUtil;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class WQLinkRegisterControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-link-register";
    private static final Integer INVALID_REP_ID = 12312334;

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;


    @BeforeEach
    void setUp(@Autowired WqLinkRegisterRepository wqLinkRegisterRepository) {
        RepositoryUtil.clearUp(wqLinkRegisterRepository);
        wqLinkRegisterRepository.save(TestEntityDataBuilder.getWQLinkRegisterEntity(8064716));
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
