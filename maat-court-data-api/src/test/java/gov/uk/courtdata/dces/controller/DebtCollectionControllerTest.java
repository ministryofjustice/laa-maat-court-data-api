package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dces.mapper.ContributionFilesMapper;
import gov.uk.courtdata.dces.mapper.ContributionFilesMapperImpl;
import gov.uk.courtdata.dces.service.DebtCollectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DebtCollectionController.class)
class DebtCollectionControllerTest {

    private static final String endpointUrl = "/api/internal/v1/debt-collection-enforcement/";
    private final ContributionFilesMapper contributionFilesMapper = new ContributionFilesMapperImpl();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DebtCollectionService debtCollectionService;

    private String contributionFileDto;

    @BeforeEach
    public void setUp() {
        contributionFileDto = TestModelDataBuilder.getContributeFileDtoJson();
    }
    @Test
    public void testContributionFileContent() throws Exception {

        final LocalDate fromDate = LocalDate.of(2020,9,1);
        final LocalDate toDate = LocalDate.of(2020,11,1);

        when(debtCollectionService.getContributionFiles(fromDate, toDate)).thenReturn(List.of("Hello"));

        mvc.perform(MockMvcRequestBuilders.get(String.format(endpointUrl+"contributions"))
                        .content(contributionFileDto)
                        .queryParam("fromDate", LocalDate.now().toString())
                        .queryParam("toDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testFdcFileContent() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get(String.format(endpointUrl+"/final-defence-cost"))
                        .content(contributionFileDto)
                        .queryParam("fromDate", LocalDate.now().toString())
                        .queryParam("toDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}