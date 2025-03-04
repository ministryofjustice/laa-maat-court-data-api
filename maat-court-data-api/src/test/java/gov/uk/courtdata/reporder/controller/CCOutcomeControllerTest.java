package gov.uk.courtdata.reporder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.reporder.service.CCOutcomeService;
import gov.uk.courtdata.reporder.validator.CCOutComeValidationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CCOutcomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class CCOutcomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CCOutComeValidationProcessor validator;

    @MockitoBean
    private CCOutcomeService service;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/cc-outcome";

    @Autowired
    private ObjectMapper objectMapper;


    private static final Integer INVALID_REP_ID = -1;


    @Test
    void givenIncorrectParameters_whenCreateIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenCreateIsInvoked_thenCreateIsSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenReturn(Optional.empty());
        when(service.create(any())).thenReturn(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutcome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void givenIncorrectParameters_whenUpdateIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenACorrectParameters_whenUpdateIsInvoked_thenShouldSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenReturn(Optional.empty());
        when(service.update(any())).thenReturn(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutcome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void givenIncorrectParameters_whenFindByRepIdIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(anyInt())).thenThrow(new ValidationException());

        mvc.perform(MockMvcRequestBuilders.get(String.format("%s/reporder/%d", ENDPOINT_URL, INVALID_REP_ID))).andExpect(status().is4xxClientError());
    }

    @Test
    void givenACorrectParameters_whenFindByRepIdIsInvoked_thenOutcomeIsSuccess() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        List repOrderCCOutComeDTOS = List.of(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        when(service.findByRepId(TestModelDataBuilder.REP_ID)).thenReturn(repOrderCCOutComeDTOS);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/reporder/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(String.valueOf("1")))
                .andExpect(jsonPath("$[0].crownCourtCode").value(String.valueOf("459")));
    }

    @Test
    void givenACorrectParameters_whenFindByRepIdIsInvoked_thenReturnOutcomeCount() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        List repOrderCCOutComeDTOS = List.of(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        when(service.findByRepId(TestModelDataBuilder.REP_ID)).thenReturn(repOrderCCOutComeDTOS);

        mvc.perform(MockMvcRequestBuilders.head(String.format("%s/reporder/%d", ENDPOINT_URL, TestModelDataBuilder.REP_ID)))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

    @Test
    void testDeleteCrownCourtOutcome() throws Exception {
        Integer repId = 1;
        when(service.deleteByRepId(repId)).thenReturn(1);

        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/rep-order/%d", ENDPOINT_URL, repId))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }


    @Test
    void testDeleteCrownCourtOutcomeWhenRepIdIsNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(String.format("%s/rep-order/", ENDPOINT_URL))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }
}