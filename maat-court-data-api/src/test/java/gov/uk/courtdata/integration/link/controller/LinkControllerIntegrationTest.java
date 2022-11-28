package gov.uk.courtdata.integration.link.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
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

    private MockMvc mockMvc;

    @Autowired
    private LinkController linkController;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;

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
        wqLinkRegisterRepository.deleteAll();
    }

    @Test
    public void testWhenMaatIdMissing_Returns400ClientError() throws Exception {

        //Request
        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        //Assert response
        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("MAAT ID is required.")));
    }


    @Test
    public void testWhenMaatIdInvalid_Returns400ClientError() throws Exception {

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(1000)
                        .build();

        String json = objectMapper.writeValueAsString(caseDetailsValidate);

        this.mockMvc.perform(post(LINK_VALIDATE_URI).content(json)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("MAAT/REP ID: 1000 is invalid.")));
    }

    @AfterEach
    public void clearUp() {
        wqLinkRegisterRepository.deleteAll();
    }
}
