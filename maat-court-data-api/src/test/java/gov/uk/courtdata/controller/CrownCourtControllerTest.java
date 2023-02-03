package gov.uk.courtdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.service.CrownCourtOutcomeService;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CrownCourtController.class)
public class CrownCourtControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/crown-court";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CrownCourtOutcomeService crownCourtOutcomeService;

    @MockBean
    private MaatIdValidator maatIdValidator;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void givenIncorrectParameters_whenUpdateCCOutcomeIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/updateCCOutcome").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenUpdateCCOutcomeIsInvoked_thenReturnStatusOK() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenReturn(Optional.empty());
        doNothing().when(crownCourtOutcomeService).updateCCOutcome(TestModelDataBuilder.getUpdateCCOutcome());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/updateCCOutcome/")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateCCOutcome())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void givenIncorrectParameters_whenUpdateCCSentenceOrderDateIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/update-cc-sentence").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenUpdateCCSentenceOrderDateIsInvoked_thenReturnStatusOK() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenReturn(Optional.empty());
        doNothing().when(crownCourtOutcomeService).updateCCSentenceOrderDate(TestModelDataBuilder.getUpdateSentenceOrder());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/update-cc-sentence")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateSentenceOrder())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void givenIncorrectParameters_whenUpdateAppealCCSentenceOrderDateIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/update-appeal-cc-sentence").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenUpdateAppealCCSentenceOrderDateIsInvoked_thenReturnStatusOK() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenReturn(Optional.empty());
        doNothing().when(crownCourtOutcomeService).updateAppealCCSentenceOrderDate(TestModelDataBuilder.getUpdateSentenceOrder());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/update-appeal-cc-sentence")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateSentenceOrder())))
                .andExpect(status().is2xxSuccessful());
    }
}