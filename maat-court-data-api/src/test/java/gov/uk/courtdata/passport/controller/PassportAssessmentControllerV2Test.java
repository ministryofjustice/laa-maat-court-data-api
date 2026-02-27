package gov.uk.courtdata.passport.controller;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.passport.service.PassportAssessmentServiceV2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PassportAssessmentControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class PassportAssessmentControllerV2Test {

    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/passport-assessments";
    
    @Autowired
    private MockMvc mvc;
    
    @MockitoBean
    PassportAssessmentServiceV2 passportAssessmentService;
    
    @Test
    void givenValidLegacyPassportAssessmentId_whenFindIsCalled_thenReturnValidPassportAssessmentResponse() throws Exception {
        when(passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenReturn(TestModelDataBuilder.getApiGetPassportedAssessmentResponse());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.legacyAssessmentId").value(LEGACY_PASSPORT_ASSESSMENT_ID));
    }

    @Test
    void givenNonExistentLegacyPassportAssessmentId_whenFindIsCalled_thenReturnBadRequestResponseError() throws Exception {
        when(passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID)).thenThrow(new RequestedObjectNotFoundException("No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_PASSPORT_ASSESSMENT_ID))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("No Passport Assessment found for ID: " + LEGACY_PASSPORT_ASSESSMENT_ID));
    }
}
