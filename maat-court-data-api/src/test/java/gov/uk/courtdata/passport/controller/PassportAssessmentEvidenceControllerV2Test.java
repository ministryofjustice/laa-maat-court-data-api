package gov.uk.courtdata.passport.controller;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.passport.service.PassportAssessmentEvidenceService;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiPassportEvidenceMetadata;
import uk.gov.justice.laa.crime.error.ProblemDetailError;

@WebMvcTest(PassportAssessmentEvidenceControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PassportAssessmentEvidenceControllerV2Test {
    
    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/passport-assessments";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    PassportAssessmentEvidenceService passportAssessmentEvidenceService;
    
    @Test
    void givenValidLegacyPassportAssessmentId_whenFindIsCalled_thenReturnValidPassportEvidenceResponse() throws Exception {
        when(passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(
            TestModelDataBuilder.getApiGetPassportedEvidenceResponse());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID + "/evidence"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.passportEvidenceMetadata").isNotEmpty())
            .andExpect(jsonPath("$.applicantEvidenceItems").isNotEmpty())
            .andExpect(jsonPath("$.partnerEvidenceItems").isNotEmpty());
    }

    @Test
    void givenNonExistentLegacyPassportAssessmentId_whenFindIsCalled_thenReturnNotFoundResponse() throws Exception {
        when(passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenThrow(new RequestedObjectNotFoundException("No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID + "/evidence"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errors.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
            .andExpect(jsonPath("$.detail").value("No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
    }

    @Test
    void givenValidLegacyPassportAssessmentIdWithNoEvidence_whenFindIsCalled_thenReturnEmptyValuesResponse() throws Exception {
        ApiGetPassportEvidenceResponse response = new ApiGetPassportEvidenceResponse();
        response.setPassportEvidenceMetadata(new ApiPassportEvidenceMetadata());
        response.setApplicantEvidenceItems(new ArrayList<>());
        response.setPartnerEvidenceItems(new ArrayList<>());
        
        when(passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(
            response);
        
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID + "/evidence"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.passportEvidenceMetadata").isEmpty())
            .andExpect(jsonPath("$.applicantEvidenceItems").isEmpty())
            .andExpect(jsonPath("$.partnerEvidenceItems").isEmpty());
    }
}
