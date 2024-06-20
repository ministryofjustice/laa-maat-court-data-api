package gov.uk.courtdata.integration.wqhearing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class WQHearingControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-hearing/";
    private static final Integer INVALID_REP_ID = 775314;
    private static final String INVALID_OFFENCE_ID = "754169aa-26895b-4bb095-a7kkb0";

    @BeforeEach
    void setUp() {
        repos.wqHearing.save(TestEntityDataBuilder.getWQHearingEntity(8064716));
    }

    @Test
    void givenAInvalidParameter_whenFindByMaatIdAndHearingUUIDIsInvoked_thenEmptyRecordsIsReturned() throws Exception {
        assertTrue(runSuccessScenario(List.of(), get(ENDPOINT_URL + INVALID_OFFENCE_ID + "/maatId/" + INVALID_REP_ID)));
    }

    @Test
    void givenAValidParameter_whenFindByMaatIdAndHearingUUIDIsInvoked_thenWQLinkRegisterIsReturned() throws Exception {
        List<WQHearingEntity> wqHearingEntityList = List.of(TestEntityDataBuilder.getWQHearingEntity(8064716));
        WQHearingEntity wqHearing =  repos.wqHearing.getReferenceById(8064716);
        wqHearingEntityList.get(0).setCreatedDateTime(wqHearing.getCreatedDateTime());
        wqHearingEntityList.get(0).setUpdatedDateTime(wqHearing.getUpdatedDateTime());
        assertTrue(runSuccessScenario(wqHearingEntityList, get(ENDPOINT_URL + TestModelDataBuilder.TEST_OFFENCE_ID + "/maatId/" + TestModelDataBuilder.REP_ID)));
    }
}
