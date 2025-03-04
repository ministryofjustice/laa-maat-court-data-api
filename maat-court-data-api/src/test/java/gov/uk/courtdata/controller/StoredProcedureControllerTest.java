package gov.uk.courtdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.service.StoredProcedureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoredProcedureController.class)
@AutoConfigureMockMvc(addFilters = false)
class StoredProcedureControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/execute-stored-procedure";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StoredProcedureService service;

    @Test
    void givenAValidContentAndErrorWhileExecuteStoredProcedure_whenExecuteStoredProcedureIsInvoked_thenErrorIsThrown() throws Exception {
        when(service.executeStoredProcedure(any())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(objectMapper.writeValueAsBytes(
                                StoredProcedureRequest.builder().build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidInput_whenExecuteStoredProcedureIsInvoked_thenReturnStatusOK() throws Exception {
        when(service.executeStoredProcedure(any())).thenReturn(ApplicationDTO.builder()
                .repId(TestModelDataBuilder.REP_ID.longValue()).build());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(objectMapper.writeValueAsBytes(
                                StoredProcedureRequest.builder().build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repId").value(TestModelDataBuilder.REP_ID.longValue()));
    }

}