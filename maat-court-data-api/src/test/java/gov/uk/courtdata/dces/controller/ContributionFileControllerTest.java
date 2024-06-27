package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.mapper.ContributionFileMapperImpl;
import gov.uk.courtdata.dces.service.ContributionFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributionFileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContributionFileControllerTest {
    private static final String BASE_URL = "/api/internal/v1/debt-collection-enforcement/contribution-file";

    private final ContributionFileMapper mapper = new ContributionFileMapperImpl();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributionFileService contributionFileService;

    @Test
    void givenCorrectParameters_whenGetContributionFileInvoked_thenResponseIsReturned() throws Exception {
        final var response = mapper.toContributionFileResponse(TestEntityDataBuilder.getPopulatedContributionFilesEntity());
        final int fileId = response.getId(); // test should fail if null
        when(contributionFileService.getContributionFile(fileId)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(fileId));
        verify(contributionFileService).getContributionFile(fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileInvoked_thenNotFound() throws Exception {
        final int INCORRECT_ID = 666;
        when(contributionFileService.getContributionFile(INCORRECT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INCORRECT_ID))
                .andExpect(status().isNotFound());
        verify(contributionFileService).getContributionFile(INCORRECT_ID);
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileInvoked_thenBadRequest() throws Exception {
        final var INVALID_ID = "crouton";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INVALID_ID))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getContributionFile(anyInt());
    }

    @Test
    void givenCorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        final var item = mapper.toContributionFileErrorResponse(TestEntityDataBuilder.getContributionFileErrorsEntity());
        final int fileId = item.getContributionFileId(); // test should fail if null
        when(contributionFileService.getAllContributionFileError(fileId)).thenReturn(List.of(item));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + fileId + "/error"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
        verify(contributionFileService).getAllContributionFileError(fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsEmpty() throws Exception {
        final int INCORRECT_ID = 666;
        when(contributionFileService.getAllContributionFileError(INCORRECT_ID)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INCORRECT_ID + "/error"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
        verify(contributionFileService).getAllContributionFileError(INCORRECT_ID);
    }

    @Test
    void givenInvalidParameters_whenGetAllContributionFileInvoked_thenBadRequest() throws Exception {
        final var INVALID_ID = "muffin";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INVALID_ID + "/error"))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getAllContributionFileError(anyInt());
    }

    @Test
    void givenCorrectParameters_whenGetContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        final var response = mapper.toContributionFileErrorResponse(TestEntityDataBuilder.getContributionFileErrorsEntity());
        final int fileId = response.getContributionFileId(); // test should fail if null
        final int contId = response.getContributionId(); // test should fail if null
        when(contributionFileService.getContributionFileError(contId, fileId)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + fileId + "/error/" + contId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.contributionFileId").value(fileId))
                .andExpect(jsonPath("$.contributionId").value(contId));
        verify(contributionFileService).getContributionFileError(contId, fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileErrorInvoked_thenNotFound() throws Exception {
        final int INCORRECT_ID = 666;
        when(contributionFileService.getContributionFileError(INCORRECT_ID, INCORRECT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INCORRECT_ID + "/error/" + INCORRECT_ID))
                .andExpect(status().isNotFound());
        verify(contributionFileService).getContributionFileError(INCORRECT_ID, INCORRECT_ID);
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileErrorInvoked_thenBadRequest() throws Exception {
        final var INVALID_ID = "crumpet";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + INVALID_ID + "/error/" + INVALID_ID))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getContributionFileError(anyInt(), anyInt());
    }
}
