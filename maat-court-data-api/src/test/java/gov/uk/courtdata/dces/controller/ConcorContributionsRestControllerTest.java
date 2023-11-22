package gov.uk.courtdata.dces.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConcorContributionsRestController.class)
class ConcorContributionsRestControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConcorContributionsService concorContributionsService;

    @Test
    void testContributionFileContent() throws Exception {

        when(concorContributionsService.getConcorFiles(ConcorContributionStatus.ACTIVE))
                .thenReturn(List.of("FirstXMLFile", "SecondXMLFile", "ThirdXMLFile"));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/concor-contribution-files"))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value("FirstXMLFile"))
                .andExpect(jsonPath("$[1]").value("SecondXMLFile"))
                .andExpect(jsonPath("$[2]").value("ThirdXMLFile"));
    }

    @Test
    void testContributionFileContentWhenActiveFileNotAvailable() throws Exception {

        when(concorContributionsService.getConcorFiles(ConcorContributionStatus.ACTIVE)).thenReturn(List.of());

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
                .contributionIds(Set.of("1", "2"))
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest)).thenReturn(true);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/update-contribution"))
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
                .contributionIds(Set.of("1", "2"))
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest))
                .thenThrow(new MAATCourtDataException("Error"));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/update-contribution"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testUpdateContributionFileStatusWhenXmlFileIsNotProvided() throws Exception {
        final ConcorContributionRequest concorContributionRequest = ConcorContributionRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .contributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(concorContributionRequest))
                .thenThrow(new ValidationException("ContributionIds are empty/null."));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/update-contribution"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("ContributionIds are empty/null."))
                .andExpect(jsonPath("code").value("BAD_REQUEST"));
    }
}