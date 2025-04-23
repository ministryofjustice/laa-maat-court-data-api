package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class ContributionFileControllerIntegrationTest extends MockMvcIntegrationTest {
    private static final String BASE_URL = "/api/internal/v1/debt-collection-enforcement/contribution-file";

    private Integer fileId;
    private Integer contributionId;

    private static final String FDC_CURRENT_XML = "<xml>CURRENT CONTENT</xml>";

    @BeforeEach
    public void setUp() {
        final var repOrder = repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder());
        final int repId = repOrder.getId();

        final var contributionFile = repos.contributionFiles.saveAndFlush(
                TestEntityDataBuilder.getPopulatedContributionFilesEntity(null, "CONTRIBUTIONS_TEST_FILE"));
        this.fileId = contributionFile.getFileId();

        final var concorContribution = ConcorContributionsEntity.builder().repId(repId).contribFileId(fileId).status(SENT).build();
        repos.concorContributions.saveAndFlush(concorContribution);
        this.contributionId = concorContribution.getId();

        repos.contributionFileErrors.saveAndFlush(
                TestEntityDataBuilder.getContributionFileErrorsEntity(fileId, contributionId));

        // Save Concor Contribution Files to verify range behaviour

        // save a future value
        saveEntityWithCreationDate("CONTRIBUTIONS_TEST_FILE_FUTURE", LocalDate.of(3000,6,15));
        saveEntityWithCreationDate("CONTRIBUTIONS_TEST_FILE_NEAR_FUTURE", LocalDate.now().plusDays(1));
        // save a long passed value
        saveEntityWithCreationDate("CONTRIBUTIONS_TEST_FILE_PAST", LocalDate.of(1000,6,15));
        saveEntityWithCreationDate("CONTRIBUTIONS_TEST_FILE_NEAR_PAST", LocalDate.now().minusDays(1));

        // Save FDC Entries
        repos.contributionFiles.saveAndFlush(
                TestEntityDataBuilder.getPopulatedContributionFilesEntity(null, "FDC_TEST_FILE_CURRENT", FDC_CURRENT_XML));
        // save a future value
        saveEntityWithCreationDate("FDC_TEST_FILE_FUTURE", LocalDate.of(3000,6,15));
        saveEntityWithCreationDate("FDC_TEST_FILE_NEAR_FUTURE", LocalDate.now().plusDays(1));
        // save a long passed value
        saveEntityWithCreationDate("FDC_TEST_FILE_PAST", LocalDate.of(1000,6,15));
        saveEntityWithCreationDate("FDC_TEST_FILE_NEAR_PAST", LocalDate.now().minusDays(1));
    }

    private void saveEntityWithCreationDate(String fileName, LocalDate creationDate){
        ContributionFilesEntity entity = repos.contributionFiles.saveAndFlush(TestEntityDataBuilder.getPopulatedContributionFilesEntity(null, fileName));
        entity.setDateCreated(creationDate);
        repos.contributionFiles.save(entity);
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

    @Test
    void givenCorrectParameters_whenGetConcorContributionFileByDateRangeInvoked_thenResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/concorFiles")
                        .param("fromDate", "2000-01-01")
                        .param("toDate", "2200-12-30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void givenExactParameters_whenGetConcorContributionFileByDateRangeInvoked_thenResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/concorFiles")
                        .param("fromDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                        .param("toDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void givenOneDayRange_whenGetFdcContributionFileByDateRangeInvoked_thenOneResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/fdcFiles")
                        .param("fromDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                        .param("toDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0]").value(FDC_CURRENT_XML));
    }

    @Test
    void givenSmallRange_whenGetFdcContributionFileByDateRangeInvoked_thenSurroundingValuesReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/fdcFiles")
                        .param("fromDate", LocalDate.now().minusDays(3).format(DateTimeFormatter.ISO_DATE))
                        .param("toDate", LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void givenHugeRange_whenGetFdcContributionFileByDateRangeInvoked_thenAllValuesAreReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/fdcFiles")
                        .param("fromDate", LocalDate.of(0,1,1).format(DateTimeFormatter.ISO_DATE))
                        .param("toDate", LocalDate.of(5000,1,1).format(DateTimeFormatter.ISO_DATE)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(5));
    }

}
