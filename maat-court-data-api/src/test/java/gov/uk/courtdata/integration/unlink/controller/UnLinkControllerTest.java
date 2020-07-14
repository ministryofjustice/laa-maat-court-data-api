package gov.uk.courtdata.integration.unlink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.controller.UnLinkController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UnLinkControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UnLinkController unLinkController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(unLinkController).build();
    }

    @Test
    public void givenUnlinkModel_whenValidationPassed() throws Exception {

        repOrderRepository.save(RepOrderEntity.builder().id(123456).build());
        wqLinkRegisterRepository.save(WqLinkRegisterEntity.builder().maatId(123456).createdTxId(1234).build());
        repOrderCPDataRepository.save(RepOrderCPDataEntity.builder().repOrderId(123456).build());

        Unlink unlink = Unlink.builder()
                .maatId(123456)
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
    public void givenUnlinkModel_whenValidationFailed() throws Exception {

        Unlink unlink = Unlink.builder()
                .maatId(null)
                .reasonId(null)
                .build();

        mockMvc.perform(post("/unlink/validate")
                .contentType("application/json")
                .header("Laa-Transaction-Id", "12112")
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }
}
