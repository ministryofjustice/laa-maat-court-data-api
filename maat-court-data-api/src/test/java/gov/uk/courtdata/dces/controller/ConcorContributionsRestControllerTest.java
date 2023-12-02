package gov.uk.courtdata.dces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConcorContributionsRestController.class)
class ConcorContributionsRestControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConcorContributionsService concorContributionsService;

    @Test
    void testContributionFileContent() throws Exception {

        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE))
                .thenReturn(List.of(
                        ConcorContributionResponse.builder().concorContributionId(1).xmlContent("FirstXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(2).xmlContent("SecondXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(3).xmlContent("ThirdXMLFile").build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/concor-contribution-files"))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[?(@.concorContributionId==1)].concorContributionId").exists())
                .andExpect(jsonPath("$.[?(@.concorContributionId==1)].xmlContent").value("FirstXMLFile"))
                .andExpect(jsonPath("$.[?(@.concorContributionId==2)].concorContributionId").exists())
                .andExpect(jsonPath("$.[?(@.concorContributionId==2)].xmlContent").value("SecondXMLFile"))
                .andExpect(jsonPath("$.[?(@.concorContributionId==3)].concorContributionId").exists())
                .andExpect(jsonPath("$.[?(@.concorContributionId==3)].xmlContent").value("ThirdXMLFile"));
    }

    @Test
    void testContributionFileContentWhenActiveFileNotAvailable() throws Exception {

        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE)).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/concor-contribution-files"))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testContributionFileContentWhenQueryParamIsNotProvided() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/concor-contribution-files"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateContributionFileStatus() throws Exception {
        final ConcorContributionRequest concorContributionRequest = ConcorContributionRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest)).thenReturn(true);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/create-contribution-file"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    void testUpdateContributionFileStatusWhenTransactionRollback() throws Exception {
        final ConcorContributionRequest concorContributionRequest = ConcorContributionRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest))
                .thenThrow(new MAATCourtDataException("Error"));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/create-contribution-file"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testUpdateContributionFileStatusWhenXmlFileIsNotProvided() throws Exception {
        final ConcorContributionRequest concorContributionRequest = ConcorContributionRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest))
                .thenThrow(new ValidationException("ContributionIds are empty/null."));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/create-contribution-file"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("ContributionIds are empty/null."))
                .andExpect(jsonPath("code").value("BAD_REQUEST"));
    }
}