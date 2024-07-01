package gov.uk.courtdata.dces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dces.request.CreateFdcContributionRequest;
import gov.uk.courtdata.dces.request.UpdateFdcContributionRequest;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FdcContributionsController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureWireMock(port = 9987)
class FdcContributionsControllerTest {

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
                .thenReturn(FdcContributionsResponse.builder()
                        .fdcContributions(List.of(
                                FdcContributionEntry.builder().id(1).finalCost(expectedCost1).build(),
                                FdcContributionEntry.builder().id(2).finalCost(expectedCost2).build(),
                                FdcContributionEntry.builder().id(3).finalCost(expectedCost3).build()))
                        .build());

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fdcContributions.length()").value(3))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==1)].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==1)].finalCost").value(expectedCost1.doubleValue()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==2)].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==2)].finalCost").value(expectedCost2.doubleValue()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==3)].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==3)].finalCost").value(expectedCost3.doubleValue()));
    }

    @Test
    void testContributionFileResponseWhenActiveFileNotAvailable() throws Exception {

        when(fdcContributionsService.getFdcContributionFiles(FdcContributionsStatus.REQUESTED))
                .thenReturn(FdcContributionsResponse.builder().fdcContributions(List.of()).build());

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fdcContributions.length()").value(0));
    }

    @Test
    void testContributionFileResponseWhenQueryParamIsNotProvided() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/fdc-contribution-files"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("Required parameter 'status' is not present."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFdcContribution() throws Exception {
        CreateFdcContributionRequest request = CreateFdcContributionRequest.builder().build();

        when(fdcContributionsService.createFdcContribution(any(CreateFdcContributionRequest.class))).thenReturn(34545);

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/fdc-contribution")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(34545));
    }

    @Test
    void testCreateFdcContributionWhenRequestIsNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/fdc-contribution")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testUpdateFdcContribution() throws Exception {
        UpdateFdcContributionRequest request = UpdateFdcContributionRequest.builder().build();
        when(fdcContributionsService.updateFdcContribution(any(UpdateFdcContributionRequest.class))).thenReturn(2);

        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/fdc-contribution")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2));
    }

    @Test
    void testUpdateFdcContributionWhenRequestIsNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/fdc-contribution")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}