package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.RepOrderCapitalRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class RepOrderCapitalIntegrationTest extends MockMvcIntegrationTest {


    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/capital";

    private static final Integer INVALID_REP_ID = -1;

    @Autowired
    private RepOrderCapitalRepository capitalRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;

    @BeforeEach
    void setUp() {

        List repOrderList = List.of(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID),
                TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID + 1));

        List repOrderCapitalList = List.of(TestEntityDataBuilder.getRepOrderCapitalEntity(1, TestModelDataBuilder.REP_ID, "SAVINGS"),
                TestEntityDataBuilder.getRepOrderCapitalEntity(2, TestModelDataBuilder.REP_ID + 1, "PROPERTY"));

        repOrderRepository.saveAllAndFlush(repOrderList);
        capitalRepository.saveAllAndFlush(repOrderCapitalList);

    }
    
    @Test
    void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorReturn() throws Exception {
        runBadRequestErrorScenario("MAAT ID is required.", head(ENDPOINT_URL + "/reporder/" + INVALID_REP_ID));
    }

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountInvoked_thenCountIsReturned() throws Exception {
        var response = runSuccessScenario(head(ENDPOINT_URL + "/reporder/" + TestEntityDataBuilder.REP_ID));
        assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");
    }

    @Test
    void givenAValidRepIdAndPropertyCapitalAsset_whenGetCapitalAssetCountInvoked_thenZeroIsReturned() throws Exception {
        var response = runSuccessScenario(head(ENDPOINT_URL + "/reporder/" + (TestEntityDataBuilder.REP_ID + 1)));
        assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("0");
    }
}
