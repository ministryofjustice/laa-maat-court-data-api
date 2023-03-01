package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.reporder.service.RepOrderCapitalService;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RepOrderCapitalController.class)
class RepOrderCapitalControllerTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/rep-orders/capital";
    private static final Integer INVALID_REP_ID = -1;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MaatIdValidator maatIdValidator;
    @MockBean
    private RepOrderCapitalService service;

    @Test
    public void givenAInvalidRepId_whenGetCapitalAssetCountIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(anyInt())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.head(endpointUrl + "/reporder/" + INVALID_REP_ID)).andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAValidRepId_whenGetCapitalAssetCountIsInvoked_thenReturnCount() throws Exception {
        when(service.getCapitalAssetCount(any())).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.head(endpointUrl + "/reporder/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

}