package gov.uk.courtdata.integration.repOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class RepOrderCapitalIntegrationTest extends MockMvcIntegrationTest {


    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/capital";

    private static final Integer INVALID_REP_ID = -1;

    private Integer REP_ID;
    private Integer MOCK1_REP_ID;

    @BeforeEach
    void setUp() {

        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        REP_ID = repOrderEntity.getId();
        RepOrderEntity repOrder = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        MOCK1_REP_ID = repOrder.getId();


        List repOrderCapitalList = List.of(TestEntityDataBuilder.getRepOrderCapitalEntity(1, REP_ID, "SAVINGS"),
                TestEntityDataBuilder.getRepOrderCapitalEntity(2, MOCK1_REP_ID, "PROPERTY"));
        repos.repOrderCapital.saveAllAndFlush(repOrderCapitalList);

    }

    @Test
    void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorReturn() throws Exception {
        runBadRequestErrorScenario("MAAT/REP ID is required, found [-1]",
            head(ENDPOINT_URL + "/reporder/" + INVALID_REP_ID));
    }

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountInvoked_thenCountIsReturned() throws Exception {
        var response = runSuccessScenario(head(ENDPOINT_URL + "/reporder/" + REP_ID));
        assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");
    }

    @Test
    void givenAValidRepIdAndPropertyCapitalAsset_whenGetCapitalAssetCountInvoked_thenZeroIsReturned() throws Exception {
        var response = runSuccessScenario(head(ENDPOINT_URL + "/reporder/" + MOCK1_REP_ID));
        assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("0");
    }
}
