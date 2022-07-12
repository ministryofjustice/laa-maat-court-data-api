package gov.uk.courtdata.integration.hardship;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.HardshipReviewDetailType;
import gov.uk.courtdata.integration.MockCdaWebConfig;
import gov.uk.courtdata.integration.MockNewWorkReasonRepository;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewDetailReason;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {MAATCourtDataApplication.class, MockCdaWebConfig.class})
public class HardshipControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String BASE_URL = "/api/internal/v1/assessment/hardship";
    private final String HARDSHIP_URL = BASE_URL + "/{hardshipId}";
    private final String HARDSHIP_BY_REP_ID_URL = BASE_URL + "/repId/{repId}";

    @Autowired
    private HardshipReviewRepository hardshipReviewRepository;
    @Autowired
    private HardshipReviewDetailRepository hardshipReviewDetailRepository;
    @Autowired
    private HardshipReviewDetailReasonRepository hardshipReviewDetailReasonRepository;
    @Autowired
    private MockNewWorkReasonRepository mockNewWorkReasonRepository;

    private HardshipReviewEntity existingHardshipReview;
    private HardshipReviewDetailReasonEntity existingHardshipReviewDetailReason;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        hardshipReviewRepository.deleteAll();
        hardshipReviewDetailRepository.deleteAll();
        hardshipReviewDetailReasonRepository.deleteAll();
        mockNewWorkReasonRepository.deleteAll();

        setupTestData();
    }

    @Test
    public void givenAHardshipReviewIdThatDoesNotExist_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        int invalidHardshipId = 9999;
        runBadRequestErrorScenario(String.format("%d is invalid", invalidHardshipId), get(HARDSHIP_URL, invalidHardshipId));
    }

    @Test
    public void givenAZeroHardshipReviewId_whenGetHardshipIsInvoked_theCorrectErrorIsReturned() throws Exception {
        runBadRequestErrorScenario("Hardship review id is required", get(HARDSHIP_URL, 0));
    }

    @Test
    public void givenAValidHardshipReviewId_whenGetHardshipIsInvoked_theCorrectDataIsReturned() throws Exception {
        runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_URL, existingHardshipReview.getId()));
    }

    @Test
    public void givenAnInvalidRepId_whenGetHardshipByRepIdIsInvoked_theCorrectErrorIsReturned() throws Exception {
        Integer repId = 9999;
        runNotFoundErrorScenario(String.format("No Hardship Review found for REP ID: %s", repId), get(HARDSHIP_BY_REP_ID_URL, repId));
    }

    @Test
    public void givenAValidRepID_whenGetHardshipByRepIdIsInvoked_theCorrectDataIsReturned() throws Exception {
        runSuccessScenario(
                getTestHardshipReviewDTO(),
                get(HARDSHIP_BY_REP_ID_URL, existingHardshipReview.getRepId()));
    }


    private void setupTestData() {
        mockNewWorkReasonRepository.save(
                TestEntityDataBuilder.getNewWorkReasonEntity());
        existingHardshipReviewDetailReason = hardshipReviewDetailReasonRepository.save(
                HardshipReviewDetailReasonEntity.builder()
                    .id(1)
                    .reason("test-reason")
                    .detailType(HardshipReviewDetailType.EXPENDITURE)
                    .userCreated("test-s").build());
        existingHardshipReview = hardshipReviewRepository.save(getTestHardshipReview());
    }

    private HardshipReviewEntity getTestHardshipReview() {
        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        HardshipReviewDetailEntity hardshipReviewDetail = TestEntityDataBuilder.getHardshipReviewDetailsEntity();
        hardshipReviewDetail.setId(null);
        hardshipReviewDetail.setDetailReason(existingHardshipReviewDetailReason);
        hardshipReview.addReviewDetail(hardshipReviewDetail);
        return hardshipReview;
    }

    private HardshipReviewDTO getTestHardshipReviewDTO() {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTO();
        hardshipReviewDTO.setId(existingHardshipReview.getId());
        hardshipReviewDTO.setUpdated(existingHardshipReview.getUpdated());
        HardshipReviewDetail reviewDetail = TestModelDataBuilder.getHardshipReviewDetail();

        reviewDetail.setId(existingHardshipReview.getReviewDetails().get(0).getId());
        reviewDetail.setDateModified(existingHardshipReview.getReviewDetails().get(0).getDateModified());

        reviewDetail.setDetailReason(
                HardshipReviewDetailReason.builder()
                        .id(existingHardshipReviewDetailReason.getId().toString())
                        .reason(existingHardshipReviewDetailReason.getReason())
                        .userCreated(existingHardshipReviewDetailReason.getUserCreated())
                        .dateCreated(existingHardshipReviewDetailReason.getDateCreated())
                        .detailType(existingHardshipReviewDetailReason.getDetailType())
                        .build());
        hardshipReviewDTO.setReviewDetails(List.of(reviewDetail));
        return hardshipReviewDTO;
    }
}
