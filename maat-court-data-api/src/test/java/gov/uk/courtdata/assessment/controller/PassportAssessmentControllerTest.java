package gov.uk.courtdata.assessment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.assessment.service.PassportAssessmentService;
import gov.uk.courtdata.assessment.validator.PassportAssessmentValidationProcessor;
import gov.uk.courtdata.builder.TestModelDataBuilder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PassportAssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PassportAssessmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;

    @MockitoBean
    private PassportAssessmentService passportAssessmentService;

    private static final String PASSPORT_ASSESSOR_DETAILS_URL =
            "/api/internal/v1/assessment/passport-assessments/{passportAssessmentId}/passport-assessor-details";

    @Test
    void givenValidPassportAssessmentId_whenFindMeansAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned()
            throws Exception {
        int passportAssessmentId = 1234;
        when(passportAssessmentService.findPassportAssessorDetails(passportAssessmentId))
                .thenReturn(TestModelDataBuilder.getAssessorDetails());

        mvc.perform(MockMvcRequestBuilders.get(PASSPORT_ASSESSOR_DETAILS_URL, passportAssessmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value("grea-k"));
    }
}
