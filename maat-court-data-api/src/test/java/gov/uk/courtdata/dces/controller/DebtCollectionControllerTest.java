package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.service.DebtCollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DebtCollectionController.class)
@AutoConfigureMockMvc(addFilters = false)
class DebtCollectionControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private DebtCollectionService debtCollectionService;

    @Test
    void givenInvalidDateFormat_whenGetContributionFiles_thenBadRequest() throws Exception {

        final LocalDate fromDate = LocalDate.of(2020,1,1);
        final LocalDate toDate = LocalDate.of(2020,11,1);

        when(debtCollectionService.getContributionFiles(fromDate, toDate)).thenReturn(List.of("Hello"));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL +"contributions"))
                        .queryParam("fromDate", "01-01-2021")
                        .queryParam("toDate", "01-11-2021")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

    }

    @Test
    void givenValidDateRange_whenGetContributionFiles_thenReturnListOfContributions() throws Exception {

        final LocalDate fromDate = LocalDate.of(2020,1,1);
        final LocalDate toDate = LocalDate.of(2020,11,1);

        when(debtCollectionService.getContributionFiles(fromDate, toDate)).thenReturn(List.of("Hello"));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL +"contributions"))
                        .queryParam("fromDate", "01.05.2021")
                        .queryParam("toDate", "01.05.2021")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidDateRange_whenGetFdcFiles_thenReturnListOfFdcFiles() throws Exception {

        final LocalDate fromDate = LocalDate.of(2020,9,1);
        final LocalDate toDate = LocalDate.of(2020,11,1);
        when(debtCollectionService.getFdcFiles(fromDate, toDate)).thenReturn(List.of("Hello"));
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL +"final-defence-cost"))
                        .queryParam("fromDate", "01.01.2021")
                        .queryParam("toDate", "01.01.2021")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void givenInvalidDateParameter_whenGetFdcFiles_thenBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL +"/final-defence-cost"))
                        .queryParam("fromDate", "01-01-2021")
                        .queryParam("toDate", "asdf")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}