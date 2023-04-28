package gov.uk.courtdata.integration.link.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.link.controller.LinkController;
import gov.uk.courtdata.model.CaseDetailsValidate;
import gov.uk.courtdata.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class LinkControllerIntegrationTest {


    private static final String LINK_VALIDATE_URI = "/link/validate";

    private static final Integer TEST_MAAT_ID = 1000;

    private static final String TEST_CASE_URN = "testUrn";

    private MockMvc mockMvc;

    @Autowired
    private LinkController linkController;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Autowired
    private PassportAssessmentRepository passportAssessmentRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        financialAssessmentRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
    }

    @Test
    public void testWhenMaatIdMissing_Returns400ClientError() throws Exception {

        //Request
        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate.builder().build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        //Assert response
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("MAAT ID is required.")));
    }


    @Test
    public void testWhenMaatIdInvalid_Returns400ClientError() throws Exception {

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is(format("MAAT/REP ID: %d is invalid.", TEST_MAAT_ID))));
    }

    @Test
    public void testWhenCaseUrnHasNoCPDataExists_Returns400ClientError() throws Exception {

        repOrderRepository.save(createRepOrderEntity(TEST_MAAT_ID));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        is(format("%d has no common platform data created against Maat application.", TEST_MAAT_ID))));
    }

    @Test
    public void testWhenCaseUrnNotExists_Returns400ClientError() throws Exception {

        repOrderRepository.save(createRepOrderEntity(TEST_MAAT_ID));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();
        caseDetailsValidate.setCaseUrn(null);

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("CaseURN can't be null or empty on request.")));
    }


    @Test
    public void testWhenMaatIdIsAlreadyLinked_Returns400ClientError() throws Exception {

        repOrderRepository.save(createRepOrderEntity(TEST_MAAT_ID));
        wqLinkRegisterRepository.save(WqLinkRegisterEntity.builder().createdTxId(0).maatId(TEST_MAAT_ID).build());
        repOrderCPDataRepository.save(createRepOrderCPDataEntity(TEST_MAAT_ID, TEST_CASE_URN));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is(format("%s is already linked to a case.", TEST_MAAT_ID))));
    }

    @Test
    public void testWhenPreConditionValidationPasses_Returns200Success() throws Exception {

        repOrderRepository.save(createRepOrderEntity(TEST_MAAT_ID));

        repOrderCPDataRepository.save(createRepOrderCPDataEntity(TEST_MAAT_ID, TEST_CASE_URN));

        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        // Assert Http Response.
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @AfterEach
    public void clearUp() {
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
    }

    public RepOrderCPDataEntity createRepOrderCPDataEntity(final Integer maatId, final String caseUrn) {
        return RepOrderCPDataEntity.builder()
                .repOrderId(maatId)
                .caseUrn(caseUrn)
                .build();
    }


    public RepOrderEntity createRepOrderEntity(final Integer maatId) {

        return RepOrderEntity.builder()
                .id(maatId)
                .dateModified(LocalDateTime.now())
                .build();
    }

    private CaseDetailsValidate getTestCaseDetailsValidate() {
        return CaseDetailsValidate.builder().maatId(TEST_MAAT_ID).caseUrn(TEST_CASE_URN).build();
    }

}
