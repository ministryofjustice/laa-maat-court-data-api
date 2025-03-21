package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class ContributionFileControllerIntegrationTest extends MockMvcIntegrationTest {
    private static final String BASE_URL = "/api/internal/v1/debt-collection-enforcement/contribution-file";

    private Integer fileId;
    private Integer contributionId;

    @BeforeEach
    public void setUp() {
        final var repOrder = repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder());
        final int repId = repOrder.getId();

        final var contributionFile = TestEntityDataBuilder.getPopulatedContributionFilesEntity(null);
        repos.contributionFiles.saveAndFlush(contributionFile);
        this.fileId = contributionFile.getFileId();

        final var concorContribution = TestEntityDataBuilder.getConcorContributionsEntity(repId, SENT);
        repos.concorContributions.saveAndFlush(concorContribution);
        this.contributionId = concorContribution.getId();

        repos.contributionFileErrors.saveAndFlush(
                TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId));
    }

    @AfterEach
    public void clearUp() {
        this.fileId = null;
        this.contributionId = null;
    }

    @Test
    void givenCorrectParameters_whenGetContributionFileInvoked_thenResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(fileId));
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileInvoked_thenNotFound() throws Exception {
        final int incorrectFileId = fileId + 666;

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", incorrectFileId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileInvoked_thenBadRequest() throws Exception {
        final String invalidFileId = "crouton";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}", invalidFileId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void givenIncorrectParameters_whenGetAllContributionFileErrorInvoked_thenResponseIsEmpty() throws Exception {
        final int incorrectFileId = fileId + 666;

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", incorrectFileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void givenInvalidParameters_whenGetAllContributionFileInvoked_thenBadRequest() throws Exception {
        final String invalidFileId = "muffin";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error", invalidFileId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenGetContributionFileErrorInvoked_thenResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", fileId, contributionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.contributionFileId").value(fileId))
                .andExpect(jsonPath("$.contributionId").value(contributionId));
    }

    @Test
    void givenIncorrectParameters_whenGetContributionFileErrorInvoked_thenNotFound() throws Exception {
        final int incorrectFileId = fileId + 666;
        final int incorrectContributionId = contributionId + 666;

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", incorrectFileId, incorrectContributionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInvalidParameters_whenGetContributionFileErrorInvoked_thenBadRequest() throws Exception {
        final String invalidFileId = "muffin";
        final String invalidContributionId = "crumpet";

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{fileId}/error/{contributionId}", invalidFileId, invalidContributionId))
                .andExpect(status().isBadRequest());
    }
}
