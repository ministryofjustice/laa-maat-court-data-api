package gov.uk.courtdata.dces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConcorContributionsRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConcorContributionsRestControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement";
    private static final String CREATE_CONTRIBUTION_FILE_URL = "/create-contribution-file";
    private static final String CONCOR_CONTRIBUTION_FILES_URL = "/concor-contribution-files";
    private static final String CONCOR_CONTRIBUTION_XML_URL = "/concor-contribution-xml";
    private static final String DRC_UPDATE_URL = "/log-contribution-response";

    private static final String CONCOR_CONTRIBUTION_STATUS_URL = "/concor-contribution-status";
    private static final String CONCOR_CONTRIBUTION_URL = "/concor-contribution";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConcorContributionsService concorContributionsService;

    @Test
    void testContributionFileContent() throws Exception {

        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE, 3, 121))
                .thenReturn(List.of(
                        ConcorContributionResponse.builder().concorContributionId(1).xmlContent("FirstXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(2).xmlContent("SecondXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(3).xmlContent("ThirdXMLFile").build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_FILES_URL))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .queryParam("concorContributionId", String.valueOf(121))
                        .queryParam("numberOfRecords", String.valueOf(3))
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
    void testContributionFileContentWhenContribIdIsNull() throws Exception {

        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE, 3, null))
                .thenReturn(List.of(
                        ConcorContributionResponse.builder().concorContributionId(1).xmlContent("FirstXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(2).xmlContent("SecondXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(3).xmlContent("ThirdXMLFile").build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_FILES_URL))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .queryParam("numberOfRecords", String.valueOf(3))
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
    void testContributionFileContentWhenNumberOfRecordIsNull() throws Exception {

        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE, null, null))
                .thenReturn(List.of(
                        ConcorContributionResponse.builder().concorContributionId(1).xmlContent("FirstXMLFile").build(),
                        ConcorContributionResponse.builder().concorContributionId(3).xmlContent("ThirdXMLFile").build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_FILES_URL))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[?(@.concorContributionId==1)].concorContributionId").exists())
                .andExpect(jsonPath("$.[?(@.concorContributionId==1)].xmlContent").value("FirstXMLFile"))
                .andExpect(jsonPath("$.[?(@.concorContributionId==3)].concorContributionId").exists())
                .andExpect(jsonPath("$.[?(@.concorContributionId==3)].xmlContent").value("ThirdXMLFile"));

    }

    @Test
    void testContributionFileContentWhenActiveFileNotAvailable() throws Exception {

        Integer numberOfRecords = 3;
        when(concorContributionsService.getConcorContributionFiles(ConcorContributionStatus.ACTIVE, numberOfRecords, null)).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_FILES_URL))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .queryParam("numberOfRecords", String.valueOf(numberOfRecords))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testContributionFileContentWhenQueryParamIsNotProvided() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_FILES_URL))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateContributionFileStatus() throws Exception {
        final CreateContributionFileRequest createContributionFileRequest = CreateContributionFileRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(createContributionFileRequest)).thenReturn(1111);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(createContributionFileRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  + CREATE_CONTRIBUTION_FILE_URL))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("1111"));
    }

    @Test
    void testUpdateContributionFileStatusWhenTransactionRollback() throws Exception {
        final CreateContributionFileRequest createContributionFileRequest = CreateContributionFileRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(createContributionFileRequest))
                .thenThrow(new MAATCourtDataException("Error"));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(createContributionFileRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  + CREATE_CONTRIBUTION_FILE_URL))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testUpdateContributionFileStatusWhenXmlFileIsNotProvided() throws Exception {
        final CreateContributionFileRequest createContributionFileRequest = CreateContributionFileRequest.builder()
                .recordsSent(123)
                .xmlContent("XMLFileContent")
                .concorContributionIds(Set.of())
                .build();
        when(concorContributionsService.createContributionAndUpdateConcorStatus(createContributionFileRequest))
                .thenThrow(new ValidationException("ContributionIds are empty/null."));

        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(createContributionFileRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  + CREATE_CONTRIBUTION_FILE_URL))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("ContributionIds are empty/null."))
                .andExpect(jsonPath("code").value("BAD_REQUEST"));
    }

    @Test
    void testLogDrcProcessedNoErrorSuccess() throws Exception {
        int id = 1234;
        String errorText = "";
        LogContributionProcessedRequest request = LogContributionProcessedRequest.builder()
                .concorId(id)
                .errorText(errorText)
                .build();
        when(concorContributionsService.logContributionProcessed(request))
                .thenReturn(1111);
        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL + DRC_UPDATE_URL))
                .content(createDrcUpdateJson(id, errorText))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("1111"));
    }

    @Test
    void testLogDrcProcessedError() throws Exception {
        int id = 1234;
        String errorText = "";
        LogContributionProcessedRequest request = LogContributionProcessedRequest.builder()
                .concorId(id)
                .errorText(errorText)
                .build();
        when(concorContributionsService.logContributionProcessed(request))
                .thenThrow(new MAATCourtDataException("Test Error"));
        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL + DRC_UPDATE_URL))
                        .content(createDrcUpdateJson(id, errorText))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("message").value("Test Error"));
    }

    @Test
    void testLogDrcProcessedNoContribFile() throws Exception {
        int id = 1234;
        String errorText = "";
        LogContributionProcessedRequest request = LogContributionProcessedRequest.builder()
                .concorId(id)
                .errorText(errorText)
                .build();
        when(concorContributionsService.logContributionProcessed(request))
                .thenThrow(new NoSuchElementException("contribution_file not found"));
        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL + DRC_UPDATE_URL))
                        .content(createDrcUpdateJson(id, errorText))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("Object Not Found"));
    }

    @Test
    void testUpdateContributionStatus() throws Exception {

        UpdateConcorContributionStatusRequest request = UpdateConcorContributionStatusRequest.builder().build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestBody = objectMapper.writeValueAsString(request);

        when(concorContributionsService.updateConcorContributionStatusAndResetContribFile(request))
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

        when(concorContributionsService.updateConcorContributionStatusAndResetContribFile(request)).thenReturn(List.of());

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

    @Test
    void givenEmptyListOfIds_whenXmlIsRequested_thenBadRequestError() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_XML_URL))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[]"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("ID List Empty"));
    }

    @Test
    void givenLongListOfIds_whenXmlIsRequested_thenBadRequestError() throws Exception {
        String longList = IntStream.rangeClosed(1, 351)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(","));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_XML_URL))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("["+longList+"]"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.detail").value("Too many IDs provided, max is 350"));
    }

    @Test
    void givenValidListOfIds_whenXmlIsRequested_thenValidResponse() throws Exception {

        when(concorContributionsService.getConcorContributionXml(any())).
            thenReturn(List.of(ConcorContributionResponse.builder()
                .concorContributionId(1)
                .xmlContent("FirstXMLFile")
                .build()));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  + CONCOR_CONTRIBUTION_XML_URL))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("[110, 120]"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].concorContributionId").value("1"))
            .andExpect(jsonPath("$.[0].xmlContent").value("FirstXMLFile"));
    }


    private String createDrcUpdateJson(int concorId, String errorText){
        return """
                {
                    "concorId" : %s,
                    "errorText" : "%s"
                }
                """.formatted(concorId, errorText);

    }

}
