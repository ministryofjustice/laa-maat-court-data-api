package gov.uk.courtdata.integration.repOrder;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderCapitalEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class RepOrderCapitalIntegrationTest extends MockMvcIntegrationTest {

    private Integer repId;
    private Integer mockRepId;
    private static final Integer INVALID_REP_ID = -1;
    private static final String CAPITAL_ASSETS_COUNT_URL = "/capital-assets/count";
    private static final String BASE_URL = "/api/internal/v1/assessment/rep-orders/";

    @BeforeEach
    void setUp() {
        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(
                TestEntityDataBuilder.getPopulatedRepOrder());
        repId = repOrderEntity.getId();
        RepOrderEntity repOrder = repos.repOrder.saveAndFlush(
                TestEntityDataBuilder.getPopulatedRepOrder());
        mockRepId = repOrder.getId();

        List<RepOrderCapitalEntity> repOrderCapitalList = List.of(
                TestEntityDataBuilder.getRepOrderCapitalEntity(1, repId, "SAVINGS"),
                TestEntityDataBuilder.getRepOrderCapitalEntity(2, mockRepId, "PROPERTY")
        );
        repos.repOrderCapital.saveAllAndFlush(repOrderCapitalList);

    }

    @Test
    void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorReturn() throws Exception {
        runBadRequestErrorScenario("MAAT/REP ID is required, found [-1]",
                get(BASE_URL + INVALID_REP_ID + CAPITAL_ASSETS_COUNT_URL));
    }

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountInvoked_thenCountIsReturned() throws Exception {
        var response = runSuccessScenario(get(BASE_URL + repId + CAPITAL_ASSETS_COUNT_URL));
        assertThat(response.getResponse().getContentAsString()).isEqualTo("1");
    }

    @Test
    void givenAValidRepIdAndPropertyCapitalAsset_whenGetCapitalAssetCountInvoked_thenZeroIsReturned()
            throws Exception {
        var response = runSuccessScenario(get(BASE_URL + mockRepId + CAPITAL_ASSETS_COUNT_URL));
        assertThat(response.getResponse().getContentAsString()).isEqualTo("0");
    }
}
