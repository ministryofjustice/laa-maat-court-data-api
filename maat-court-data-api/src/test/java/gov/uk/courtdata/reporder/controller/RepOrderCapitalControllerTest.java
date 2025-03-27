package gov.uk.courtdata.reporder.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.reporder.service.RepOrderCapitalService;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RepOrderCapitalController.class)
class RepOrderCapitalControllerTest {

    private static final Integer INVALID_REP_ID = -1;
    private static final String CAPITAL_ASSETS_COUNT_URL = "/capital-assets/count";
    private static final String BASE_URL = "/api/internal/v1/assessment/rep-orders/";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MaatIdValidator maatIdValidator;

    @MockitoBean
    private RepOrderCapitalService service;


    @Test
    void givenInvalidRoute_whenRequestIsMade_thenNotFoundIsReturned()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + INVALID_REP_ID + "/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(anyInt())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(
                        BASE_URL + "/" + INVALID_REP_ID + CAPITAL_ASSETS_COUNT_URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountIsInvoked_thenReturnCount() throws Exception {
        when(service.getCapitalAssetCount(any())).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.get(
                        BASE_URL + "/" + TestModelDataBuilder.REP_ID + CAPITAL_ASSETS_COUNT_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}