package gov.uk.courtdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.service.CrownCourtOutcomeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CrownCourtController.class)
public class CrownCourtControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/crown-court";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CrownCourtOutcomeService crownCourtOutcomeService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void givenIncorrectParameters_whenUpdateCCOutcomeIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/updateCCOutcome").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenUpdateCCOutcomeIsInvoked_thenReturnStatusOK() throws Exception {
        doNothing().when(crownCourtOutcomeService).update(TestModelDataBuilder.getUpdateCCOutcome());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/updateCCOutcome/")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateCCOutcome())))
                .andExpect(status().is2xxSuccessful());
    }
}