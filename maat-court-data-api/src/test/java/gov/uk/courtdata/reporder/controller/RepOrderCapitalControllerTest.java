package gov.uk.courtdata.reporder.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.reporder.service.RepOrderCapitalService;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RepOrderCapitalController.class)
class RepOrderCapitalControllerTest {

    private static final Integer INVALID_REP_ID = -1;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MaatIdValidator maatIdValidator;

    @MockitoBean
    private RepOrderCapitalService service;

    @Test
    void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(anyInt())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + INVALID_REP_ID + "/capital-assets/count"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountIsInvoked_thenReturnCount() throws Exception {
        when(service.getCapitalAssetCount(any())).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.head(
                        ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/capital-assets/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

}