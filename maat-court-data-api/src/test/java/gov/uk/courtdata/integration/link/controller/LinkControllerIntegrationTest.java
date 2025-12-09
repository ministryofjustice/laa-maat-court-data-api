package gov.uk.courtdata.integration.link.controller;


import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.link.controller.LinkController;
import gov.uk.courtdata.model.CaseDetailsValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@WebAppConfiguration
class LinkControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String LINK_VALIDATE_URI = "/link/validate";
    private static final String TEST_CASE_URN = "testUrn";
    private Integer TEST_MAAT_ID = 1000;
    private MockMvc mockMvc;

    @Autowired
    private LinkController linkController;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testWhenMaatIdMissing_Returns400ClientError() throws Exception {

        //Request
        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate.builder().build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        //Assert response
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("MAAT/REP ID is required, found [null]"));
    }


    @Test
    void testWhenMaatIdInvalid_Returns400ClientError() throws Exception {

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(
                        jsonPath("$.message").value(
                                format("MAAT/REP ID [%d] is invalid", TEST_MAAT_ID)));
    }

    @Test
    void testWhenCaseUrnHasNoCPDataExists_Returns400ClientError() throws Exception {

        createRepOrderEntity();

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(
                        format("%d has no common platform data created against Maat application.",
                                TEST_MAAT_ID)));
    }

    @Test
    void testWhenCaseUrnNotExists_Returns400ClientError() throws Exception {

        createRepOrderEntity();

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();
        caseDetailsValidate.setCaseUrn(null);

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(
                        jsonPath("$.message").value("CaseURN can't be null or empty on request."));
    }


    @Test
    void testWhenMaatIdIsAlreadyLinked_Returns400ClientError() throws Exception {

        createRepOrderEntity();
        repos.wqLinkRegister.save(
                WqLinkRegisterEntity.builder().createdTxId(0).maatId(TEST_MAAT_ID).build());
        repos.repOrderCPData.save(createRepOrderCPDataEntity(TEST_MAAT_ID, TEST_CASE_URN));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(
                        format("%s is already linked to a case.", TEST_MAAT_ID)));
    }

    @Test
    void testWhenPreConditionValidationPasses_Returns200Success() throws Exception {

        createRepOrderEntity();

        repos.repOrderCPData.save(createRepOrderCPDataEntity(TEST_MAAT_ID, TEST_CASE_URN));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    public RepOrderCPDataEntity createRepOrderCPDataEntity(final Integer maatId,
            final String caseUrn) {
        return RepOrderCPDataEntity.builder()
                .repOrderId(maatId)
                .caseUrn(caseUrn)
                .build();
    }


    public void createRepOrderEntity() {
        RepOrderEntity repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        TEST_MAAT_ID = repOrder.getId();
    }

    private CaseDetailsValidate getTestCaseDetailsValidate() {
        return CaseDetailsValidate.builder().maatId(TEST_MAAT_ID).caseUrn(TEST_CASE_URN).build();
    }

}
