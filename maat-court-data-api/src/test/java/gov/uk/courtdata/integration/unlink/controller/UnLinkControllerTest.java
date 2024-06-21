package gov.uk.courtdata.integration.unlink.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.controller.UnLinkController;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@AutoConfigureMockMvc
public class UnLinkControllerTest extends MockMvcIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UnLinkController unLinkController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(unLinkController).build();
    }

    @Test
    public void givenUnlinkModel_whenValidationPassed() throws Exception {

        RepOrderEntity repOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        repos.wqLinkRegister.save(
            WqLinkRegisterEntity.builder().maatId(repOrder.getId()).createdTxId(1234).build());
        repos.repOrderCPData.save(
            RepOrderCPDataEntity.builder().repOrderId(repOrder.getId()).build());

        Unlink unlink = Unlink.builder()
                .maatId(repOrder.getId())
                .reasonId(1)
                .userId("User")
                .laaTransactionId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/unlink/validate")
                        .contentType("application/json")
                        .header("Laa-Transaction-Id", "12112")
                        .content(objectMapper.writeValueAsString(unlink)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUnlinkModel_whenValidationFailedTX() throws Exception {

        mockMvc.perform(post("/unlink/validate")
                        .contentType("application/json")
                        .header("Laa-Transaction-Id", "12112")
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUnlinkModel_whenValidationFailed() throws Exception {

        mockMvc.perform(post("/unlink/validate")
                        .contentType("application/json")
                        .header("Laa-Transaction-Id", "12112")
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }
}
