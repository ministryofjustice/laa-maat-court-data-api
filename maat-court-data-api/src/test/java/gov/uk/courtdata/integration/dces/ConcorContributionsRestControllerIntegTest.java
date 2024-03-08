package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConcorContributionsRestControllerIntegTest extends MockMvcIntegrationTest {

    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-contribution-file";

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/concor-contribution-files?status=";

    @Autowired
    private ConcorContributionsRepository concorRepository;

    @BeforeEach
    public void setUp() {
        concorRepository.saveAll(List.of(
                ConcorContributionsEntity.builder().id(1).status(ConcorContributionStatus.ACTIVE).currentXml("<xml 1 content dummy").build(),
                ConcorContributionsEntity.builder().id(2).status(ConcorContributionStatus.SENT).currentXml("<xml 2 content dummy").build(),
                ConcorContributionsEntity.builder().id(3).status(ConcorContributionStatus.ACTIVE).currentXml("<xml 3 content dummy").build(),
                ConcorContributionsEntity.builder().id(4).status(ConcorContributionStatus.SENT).currentXml("<xml 4 content dummy").build()
        ));
    }

    @Test
    @Order(1)
    void givenAACTIVEStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(2)
    void givenAREPLACEDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"REPLACED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(3)
    void givenAnInvalidStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"XXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'XXX' is the incorrect type for the 'status' parameter."));
    }

    @Disabled("Update SQL is not understood.")
    @Test
    void givenAListOfIds_whenAtomicUpdate_theStatusIsUpdated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ATOMIC_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)

                        .content("""
                                {
                                    "recordsSent": 2,
                                    "concorContributionIds": ["1234","9876"],
                                    "xmlContent" : "<test></test>",
                                    "ackXmlContent" : "<ackTest></ackTest>",
                                    "xmlFileName" : "TestFilename.xml"
                                }"""))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'RUBBISH_VALUE' is the incorrect type for the 'status' parameter."));
    }

}