package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.service.DebtCollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DebtCollectionController.class)
class DebtCollectionControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DebtCollectionService debtCollectionService;

    @Test
    void testContributionFileContent_FailScenario() throws Exception {

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
    void testContributionFileContent() throws Exception {

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
    void testFdcFileContent() throws Exception {

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
    void givenIncorrectDateParameter_whenApiIsInvoked_then400ErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL +"/final-defence-cost"))
                        .queryParam("fromDate", "01-01-2021")
                        .queryParam("toDate", "asdf")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}