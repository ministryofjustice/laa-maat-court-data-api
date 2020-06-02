package gov.uk.courtdata.integrationTest.unlink;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.integrationTest.MockServicesConfig;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.controller.UnLinkController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@AutoConfigureMockMvc
public class UnLinkControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private UnLinkController unLinkController;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(unLinkController).build();
    }

    @Test
    public void givenUnlinkModel_whenValidationPassed () throws Exception {

        Unlink unlink = Unlink.builder()
                .maatId(123456)
                .reasonId(999888)
                .laaTransactionId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/unlink/validate")
                .contentType("application/json")
                .header("Laa-Transaction-Id","12112")
                .content(objectMapper.writeValueAsString(unlink)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUnlinkModel_whenValidationFailedTX () throws Exception {

        Unlink unlink = Unlink.builder()
                .maatId(null)
                .reasonId(null)
                .build();

        mockMvc.perform(post("/unlink/validate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(unlink)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUnlinkModel_whenValidationFailed () throws Exception {

        Unlink unlink = Unlink.builder()
                .maatId(null)
                .reasonId(null)
                .build();

        mockMvc.perform(post("/unlink/validate")
                .contentType("application/json")
                .header("Laa-Transaction-Id","12112")
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

}
