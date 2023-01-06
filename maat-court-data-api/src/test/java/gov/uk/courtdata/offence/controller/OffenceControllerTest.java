package gov.uk.courtdata.offence.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.offence.service.OffenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OffenceController.class)
public class OffenceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OffenceService offenceService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/offence";

    @Test
    void givenIncorrectParameters_whenFindOffenceByCaseIdIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/case/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenFindOffenceByCaseIdIsInvoked_thenReturnOffence() throws Exception {
        List offenceDTOList = List.of(TestModelDataBuilder.getOffenceDTO(313123));
        when(offenceService.findByCaseId(TestModelDataBuilder.TEST_CASE_ID)).thenReturn(offenceDTOList);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/case/" + TestModelDataBuilder.TEST_CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].caseId").value(String.valueOf(TestModelDataBuilder.TEST_CASE_ID)));
    }

    @Test
    void givenAValidParameters_whenGetNewOffenceCountIsInvoked_thenReturnNewOffenceCount() throws Exception {
        when(offenceService.getNewOffenceCount(TestModelDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID)).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.TEST_OFFENCE_ID + "/case/" + TestModelDataBuilder.TEST_CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

}