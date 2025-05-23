package gov.uk.courtdata.offence.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.OffenceDTO;
import gov.uk.courtdata.offence.service.OffenceService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(OffenceController.class)
@AutoConfigureMockMvc(addFilters = false)
class OffenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OffenceService offenceService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/offence";

    @Test
    void givenInvalidRoute_whenRequestIsMade_thenNotFoundIsReturned()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/case/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidCaseId_whenFindOffenceByCaseIdIsInvoked_thenReturnOffence() throws Exception {
        List<OffenceDTO> offenceDTOList =
                List.of(TestModelDataBuilder.getOffenceDTO(313123));
        when(offenceService.findByCaseId(TestModelDataBuilder.TEST_CASE_ID))
                .thenReturn(offenceDTOList);
        mvc.perform(MockMvcRequestBuilders.get(
                        ENDPOINT_URL + "/case/" + TestModelDataBuilder.TEST_CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].caseId").value(
                        String.valueOf(TestModelDataBuilder.TEST_CASE_ID)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void givenValidParameters_whenGetNewOffenceCountIsInvoked_thenReturnCorrectNewOffenceCount(
            int expectedCount) throws Exception {
        when(offenceService.getNewOffenceCount(TestModelDataBuilder.TEST_CASE_ID,
                TestModelDataBuilder.TEST_OFFENCE_ID)).thenReturn(expectedCount);
        mvc.perform(MockMvcRequestBuilders.get(
                        ENDPOINT_URL + "/" + TestModelDataBuilder.TEST_OFFENCE_ID + "/case/"
                                + TestModelDataBuilder.TEST_CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCount)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}