package gov.uk.courtdata.integration.passport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ProblemDetailError;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class PassportControllerV2IntegrationTest extends MockMvcIntegrationTest {

    private int repId;
    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/passport-assessments";

    @Autowired
    private ObjectMapper objectMapper;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder());
        repId = repOrderEntity.getId();
    }

    @Test
    void givenValidPassportAssessmentId_whenRollbackIsInvoked_thenPassportAssessmentIsRolledBack() throws Exception {
        PassportAssessmentEntity passportAssessmentEntity =
                repos.passportAssessment.save(TestEntityDataBuilder.getPassportAssessmentEntity(repId));
        Integer passportAssessmentId = passportAssessmentEntity.getId();

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/" + passportAssessmentId + "/rollback"))
                .andExpect(status().isOk());

        Optional<PassportAssessmentEntity> rolledBackPassportAssessmentEntity =
                Optional.ofNullable(repos.passportAssessment.findByRepOrderId(repId));
        assertThat(rolledBackPassportAssessmentEntity).isNotNull();
        assertThat(rolledBackPassportAssessmentEntity)
                .hasValueSatisfying(passportAssessment ->
                        assertThat(passportAssessment.getValid()).isFalse());
    }

    @Test
    void givenNonExistentPassportAssessmentId_whenRollbackIsInvoked_thenNotFoundProblemDetailIsReturned()
            throws Exception {

        int nonExistentPassportAssessmentId = Integer.MAX_VALUE;

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL + "/" + nonExistentPassportAssessmentId + "/rollback"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();

        ProblemDetail problemDetail =
                ProblemDetailUtil.parseProblemDetailJson(result.getResponse().getContentAsString());
        softly.assertThat(problemDetail)
                .hasFieldOrPropertyWithValue("type", URI.create("about:blank"))
                .hasFieldOrPropertyWithValue("title", "Not Found")
                .hasFieldOrPropertyWithValue(
                        "detail", "No Passported Assessment found for ID: " + nonExistentPassportAssessmentId)
                .hasFieldOrPropertyWithValue("status", 404)
                .hasFieldOrPropertyWithValue(
                        "instance", URI.create(ENDPOINT_URL + "/" + nonExistentPassportAssessmentId + "/rollback"));
        Optional<ErrorExtension> extension = ProblemDetailUtil.getErrorExtension(problemDetail);
        softly.assertThat(extension)
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("code", ProblemDetailError.OBJECT_NOT_FOUND.code())
                .hasFieldOrPropertyWithValue("errors", List.of());
    }
}
