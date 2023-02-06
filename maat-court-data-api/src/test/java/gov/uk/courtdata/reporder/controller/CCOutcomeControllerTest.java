package gov.uk.courtdata.reporder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.reporder.service.CCOutcomeService;
import gov.uk.courtdata.reporder.validator.CCOutComeValidationProcessor;
import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CCOutcomeController.class)
class CCOutcomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CCOutComeValidationProcessor validator;

    @MockBean
    private CCOutcomeService service;

    private static final String endpointUrl = "/api/internal/v1/assessment/rep-orders/cc-outcome";

    @Autowired
    private ObjectMapper objectMapper;


    private static final Integer INVALID_REP_ID = -1;


    @Test
    void givenIncorrectParameters_whenCreateIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenCreateIsInvoked_thenCreateIsSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenReturn(Optional.empty());
        when(service.create(any())).thenReturn(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutcome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void givenIncorrectParameters_whenUpdateIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenACorrectParameters_whenUpdateIsInvoked_thenShouldSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutcome.class))).thenReturn(Optional.empty());
        when(service.update(any())).thenReturn(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1234));
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutcome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenFindByRepIdIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(anyInt())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + INVALID_REP_ID)).andExpect(status().is4xxClientError());
    }

    @Test
    void givenACorrectParameters_whenFindByRepIdIsInvoked_thenOutcomeIsSuccess() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        List repOrderCCOutComeDTOS = List.of(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1));
        when(service.findByRepId(TestModelDataBuilder.REP_ID)).thenReturn(repOrderCCOutComeDTOS);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].crownCourtCode").value(String.valueOf("459")));
    }
}