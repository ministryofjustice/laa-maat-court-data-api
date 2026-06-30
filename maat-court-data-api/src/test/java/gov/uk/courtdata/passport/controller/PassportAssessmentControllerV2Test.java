package gov.uk.courtdata.passport.controller;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.passport.service.PassportAssessmentServiceV2;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ProblemDetailError;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PassportAssessmentControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PassportAssessmentControllerV2Test {

    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/passport-assessments";
    private static final String ROLLBACK_URL = ENDPOINT_URL + "/{id}/rollback";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    PassportAssessmentServiceV2 passportAssessmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidLegacyPassportAssessmentId_whenFindIsCalled_thenReturnValidPassportAssessmentResponse()
            throws Exception {
        when(passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID))
                .thenReturn(TestModelDataBuilder.getApiGetPassportedAssessmentResponse());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAssessmentId").value(LEGACY_PASSPORT_ASSESSMENT_ID));
    }

    @Test
    void givenNonExistentLegacyPassportAssessmentId_whenFindIsCalled_thenReturnNotFoundResponse() throws Exception {
        when(passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID))
                .thenThrow(new RequestedObjectNotFoundException(
                        "No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errorContext.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
                .andExpect(jsonPath("$.detail")
                        .value("No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
    }

    @Test
    void givenFullRequest_whenCreateIsCalled_thenReturnSuccessfulResponse() throws Exception {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        var expectedEntity = TestModelDataBuilder.buildValidCreatePassportedAssessmentResponse();
        when(passportAssessmentService.create(any())).thenReturn(expectedEntity);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAssessmentId").value(expectedEntity.getLegacyAssessmentId()));
    }

    @Test
    void givenAPartialRequest_whenCreateIsCalled_thenReturnValidationFailureResponse() throws Exception {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        request.getPassportedAssessment().setAssessmentReason(null);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("Validation failure"));
        verify(passportAssessmentService, never()).create(any());
    }

    @Test
    void givenMaatDatabaseException_whenCreateIsCalled_thenReturnCorrectErrorResponse() throws Exception {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        when(passportAssessmentService.create(any())).thenThrow(new DataIntegrityViolationException("Test Error"));
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("Request violates a data constraint"));
        verify(passportAssessmentService, times(1)).create(any());
    }

    @Test
    void givenAPassportAssessmentId_whenRollbackIsCalled_thenOkResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ROLLBACK_URL, LEGACY_PASSPORT_ASSESSMENT_ID))
                .andExpect(status().isOk());

        verify(passportAssessmentService).rollback(LEGACY_PASSPORT_ASSESSMENT_ID);
    }

    @Test
    void givenAnInvalidPassportAssessmentId_whenRollbackIsCalled_thenNotFoundResponseIsReturned() throws Exception {
        String exceptionMessage =
                String.format("No Passported Assessment found for ID: %d", LEGACY_PASSPORT_ASSESSMENT_ID);

        doThrow(new RequestedObjectNotFoundException(exceptionMessage))
                .when(passportAssessmentService)
                .rollback(LEGACY_PASSPORT_ASSESSMENT_ID);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(ROLLBACK_URL, LEGACY_PASSPORT_ASSESSMENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();

        validateProblemDetailResponse(
                result.getResponse().getContentAsString(),
                ProblemDetailError.OBJECT_NOT_FOUND.code(),
                "No Passported Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID);
    }

    private void validateProblemDetailResponse(String responseString, String expectedCode, String expectedDetail)
            throws JsonProcessingException {
        ProblemDetail problemDetail = ProblemDetailUtil.parseProblemDetailJson(responseString);
        assertThat(problemDetail.getDetail()).isEqualTo(expectedDetail);
        Optional<ErrorExtension> extension = ProblemDetailUtil.getErrorExtension(problemDetail);
        assertThat(extension).isPresent().get().hasFieldOrPropertyWithValue("code", expectedCode);
    }
}
