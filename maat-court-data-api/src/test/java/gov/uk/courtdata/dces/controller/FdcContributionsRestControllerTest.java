package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FdcContributionsRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class FdcContributionsRestControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FdcContributionsService fdcContributionsService;

    @Test
    void testContributionFileContent() throws Exception {
        BigDecimal expectedCost1= new BigDecimal("100.1");
        BigDecimal expectedCost2= new BigDecimal("444.44");
        BigDecimal expectedCost3= new BigDecimal("999.99");

        when(fdcContributionsService.getFdcContributionFiles(FdcContributionsStatus.REQUESTED))
                .thenReturn(List.of(
                        FdcContributionsResponse.builder().id(1).finalCost(expectedCost1).build(),
                        FdcContributionsResponse.builder().id(2).finalCost(expectedCost2).build(),
                        FdcContributionsResponse.builder().id(3).finalCost(expectedCost3).build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[?(@.id==1)].id").exists())
                .andExpect(jsonPath("$.[?(@.id==1)].finalCost").value(expectedCost1.doubleValue()))
                .andExpect(jsonPath("$.[?(@.id==2)].id").exists())
                .andExpect(jsonPath("$.[?(@.id==2)].finalCost").value(expectedCost2.doubleValue()))
                .andExpect(jsonPath("$.[?(@.id==3)].id").exists())
                .andExpect(jsonPath("$.[?(@.id==3)].finalCost").value(expectedCost3.doubleValue()));
    }

    @Test
    void testContributionFileContentWhenActiveFileNotAvailable() throws Exception {

        when(fdcContributionsService.getFdcContributionFiles(FdcContributionsStatus.REQUESTED)).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testContributionFileContentWhenQueryParamIsNotProvided() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

}