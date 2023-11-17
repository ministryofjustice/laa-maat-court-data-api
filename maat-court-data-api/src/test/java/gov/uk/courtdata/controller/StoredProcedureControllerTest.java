package gov.uk.courtdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.service.StoredProcedureService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StoredProcedureController.class)
class StoredProcedureControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/execute-stored-procedure";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoredProcedureService service;

    @Test
    void givenInvalidContent_whenExecuteStoredProcedureIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidInput_whenExecuteStoredProcedureIsInvoked_thenReturnStatusOK() throws Exception {
        when(service.executeStoredProcedure(any())).thenReturn(ApplicationDTO.builder()
                .repId(TestModelDataBuilder.REP_ID.longValue()).build());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.repId").value(TestModelDataBuilder.REP_ID.longValue()));
    }

}