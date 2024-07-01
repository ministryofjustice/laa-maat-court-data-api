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
        final int fileId = 99;
        final var response = mapper.toContributionFileResponse(TestEntityDataBuilder.getPopulatedContributionFilesEntity(fileId));
        when(contributionFileService.getContributionFile(fileId)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(fileId));
        verify(contributionFileService).getContributionFile(fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileInvoked_thenNotFound() throws Exception {
        final int INCORRECT_FILE_ID = 666;
        when(contributionFileService.getContributionFile(INCORRECT_FILE_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", INCORRECT_FILE_ID))
                .andExpect(status().isNotFound());
        verify(contributionFileService).getContributionFile(INCORRECT_FILE_ID);
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileInvoked_thenBadRequest() throws Exception {
        final String INVALID_FILE_ID = "crouton";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", INVALID_FILE_ID))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getContributionFile(anyInt());
    }

    @Test
    void givenCorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        final int fileId = 99, contributionId = 888;
        final var item = mapper.toContributionFileErrorResponse(TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId));
        when(contributionFileService.getAllContributionFileError(fileId)).thenReturn(List.of(item));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
        verify(contributionFileService).getAllContributionFileError(fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsEmpty() throws Exception {
        final int INCORRECT_FILE_ID = 666;
        when(contributionFileService.getAllContributionFileError(INCORRECT_FILE_ID)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", INCORRECT_FILE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
        verify(contributionFileService).getAllContributionFileError(INCORRECT_FILE_ID);
    }

    @Test
    void givenInvalidParameters_whenGetAllContributionFileInvoked_thenBadRequest() throws Exception {
        final String INVALID_FILE_ID = "muffin";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", INVALID_FILE_ID))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getAllContributionFileError(anyInt());
    }

    @Test
    void givenCorrectParameters_whenGetContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        final int fileId = 99, contributionId = 888;
        final var response = mapper.toContributionFileErrorResponse(TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId));
        when(contributionFileService.getContributionFileError(contributionId, fileId)).thenReturn(Optional.of(response));

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", fileId, contributionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.contributionFileId").value(fileId))
                .andExpect(jsonPath("$.contributionId").value(contributionId));
        verify(contributionFileService).getContributionFileError(contributionId, fileId);
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileErrorInvoked_thenNotFound() throws Exception {
        final int INCORRECT_FILE_ID = 666, INCORRECT_CONTRIBUTION_ID = 666;
        when(contributionFileService.getContributionFileError(INCORRECT_FILE_ID, INCORRECT_CONTRIBUTION_ID)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", INCORRECT_FILE_ID, INCORRECT_CONTRIBUTION_ID))
                .andExpect(status().isNotFound());
        verify(contributionFileService).getContributionFileError(INCORRECT_FILE_ID, INCORRECT_CONTRIBUTION_ID);
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileErrorInvoked_thenBadRequest() throws Exception {
        final String INVALID_FILE_ID = "muffin", INVALID_CONTRIBUTION_ID = "crumpet";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", INVALID_FILE_ID, INVALID_CONTRIBUTION_ID))
                .andExpect(status().isBadRequest());
        verify(contributionFileService, never()).getContributionFileError(anyInt(), anyInt());
    }
}
