package gov.uk.courtdata.dces.controller.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnabledIf(expression = "#{environment['sentry.environment'] == 'development'}", loadContext = true)
@WebMvcTest(ConcorContributionsTestDataController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConcorContributionsTestDataControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/test-data";
    private static final String CONCOR_CONTRIBUTION_STATUS_URL = "/concor-contribution-status";
    private static final String CONCOR_CONTRIBUTION_URL = "/concor-contribution";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConcorContributionsService concorContributionsService;

    @Test
    void testUpdateContributionStatus() throws Exception {

        UpdateConcorContributionStatusRequest request = UpdateConcorContributionStatusRequest.builder().build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(request);

        when(concorContributionsService.updateConcorContributionStatus(request))
                .thenReturn(List.of(111,222, 333));

        mvc.perform(MockMvcRequestBuilders.put(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_STATUS_URL))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0]").value(111L))
                .andExpect(jsonPath("$.[1]").value(222L))
                .andExpect(jsonPath("$.[2]").value(333L));
    }

    @Test
    void testUpdateContributionStatusWhenNotFound() throws Exception {

        UpdateConcorContributionStatusRequest request = UpdateConcorContributionStatusRequest.builder().build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(request);

        when(concorContributionsService.updateConcorContributionStatus(request)).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.put(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_STATUS_URL))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void testGetContributionWhenFound() throws Exception {

        Integer id = 100;
        ConcorContributionResponseDTO responseDTO = ConcorContributionResponseDTO.builder()
                .id(id)
                .status(SENT)
                .build();

        when(concorContributionsService.getConcorContribution(id)).thenReturn(responseDTO);
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL + CONCOR_CONTRIBUTION_URL+"/100"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("SENT"));
    }
}