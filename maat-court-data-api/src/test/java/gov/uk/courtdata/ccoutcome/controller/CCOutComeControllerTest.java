package gov.uk.courtdata.ccoutcome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.ccoutcome.service.CCOutComeService;
import gov.uk.courtdata.ccoutcome.validator.CCOutComeValidator;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
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
@WebMvcTest(CCOutComeController.class)
class CCOutComeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CCOutComeValidator validator;

    @MockBean
    private CCOutComeService service;

    private static final String endpointUrl = "/api/internal/v1/assessment/cc-outcome";

    @Autowired
    private ObjectMapper objectMapper;


    private static final Integer INVALID_REP_ID = -1;


    @Test
    public void givenIncorrectParameters_whenCreateCCOutComeIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutCome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenACorrectParameters_whenCreateCCOutComeIsInvoked_thenShouldSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutCome.class))).thenReturn(Optional.empty());
        when(service.createCCOutCome(any())).thenReturn(TestModelDataBuilder.getRepOrderCCOutComeDTO());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutCome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenUpdateCCOutComeIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any(RepOrderCCOutCome.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenACorrectParameters_whenUpdateCCOutComeIsInvoked_thenShouldSuccess() throws Exception {
        when(validator.validate(any(RepOrderCCOutCome.class))).thenReturn(Optional.empty());
        doNothing().when(service).updateCCOutcome(any(RepOrderCCOutCome.class));
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderCCOutCome()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenFindByRepIdIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(anyInt())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + INVALID_REP_ID)).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenACorrectParameters_whenFindByRepIdIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        List repOrderCCOutComeDTOS = List.of(TestModelDataBuilder.getRepOrderCCOutComeDTO());
        when(service.findByRepId(TestModelDataBuilder.REP_ID)).thenReturn(repOrderCCOutComeDTOS);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].crownCourtCode").value(String.valueOf("459")));
    }
}