package gov.uk.courtdata.integration.link.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CourtHouseCodesEntity;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.link.service.CreateLinkListener;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CreateLinkListenerIntegrationTest extends MockMvcIntegrationTest {

    @Autowired
    private TestModelDataBuilder testModelDataBuilder;

    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;

    @Autowired
    private CreateLinkListener createLinkListener;

    @Autowired
    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    @Test
    public void givenSaveAndLinkModel_whenSaveAndImplIsInvoked_thenLinkEstablished() {

        //given
        RepOrderEntity repOrderEntity = repos.repOrder.save(
            TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderEntity();
        repOrderCPDataEntity.setRepOrderId(repOrderEntity.getId());
        repos.repOrderCPData.save(repOrderCPDataEntity);

        repos.courtHouseCodes.save(
            CourtHouseCodesEntity.builder().code("B16BG").effectiveDateFrom(LocalDateTime.now())
                .build());
        repos.solicitorMAATData.save(
            testEntityDataBuilder.getSolicitorMAATDataEntity(repOrderEntity.getId()));
        repos.defendantMAATData.save(
            testEntityDataBuilder.getDefendantMAATDataEntity(repOrderEntity.getId()));

        String saveAndLinkMessage = testModelDataBuilder.getSaveAndLinkString(repOrderEntity.getId());

        //when
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId", "AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        //when
        createLinkListener.receive(saveAndLinkMessage, headers);

        //then
        CourtDataDTO courtDataDTO = testModelDataBuilder.getSaveAndLinkModelRaw(repOrderEntity.getId());

        verifyWqLinkRegister(courtDataDTO);
        verifyRepOrder(courtDataDTO);

        queueMessageLogTestHelper.assertQueueMessageLogged(saveAndLinkMessage, 1, "e439dfc8-664e-4c8e-a999-d756dcf112c2", repOrderEntity.getId());

    }

    @Test
    public void givenNewMessageInSqs_whenMaatIsNull_thenThrowException() {

        String saveAndLinkMessage = getSaveAndLinkString();

        //when
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId", "AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        //when
        createLinkListener.receive(saveAndLinkMessage, headers);
        //then
        assertThat(repos.wqLinkRegister.findAll().size()).isEqualTo(0);
    }

    private void verifyRepOrder(CourtDataDTO courtDataDTO) {
        // Verify CP Rep Order Record is created
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Optional<RepOrderCPDataEntity> retrievedRepOrderEntity = repos.repOrderCPData.findByrepOrderId(
            caseDetails.getMaatId());
        RepOrderCPDataEntity found = retrievedRepOrderEntity.orElse(null);

        assertNotNull(found);
        assertThat(found.getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(caseDetails.getMaatId());
        assertThat(found.getDefendantId()).isEqualTo(caseDetails.getDefendant().getDefendantId());
    }


    private void verifyWqLinkRegister(CourtDataDTO courtDataDTO) {
        // Verify WQCore Link register Record is created
        List<WqLinkRegisterEntity> retrievedWqLinkRegisterEntity = repos.wqLinkRegister.findBymaatId(
            courtDataDTO.getCaseDetails().getMaatId());
        WqLinkRegisterEntity wqLinkRegisterEntity = retrievedWqLinkRegisterEntity.get(0);

        assertNotNull(wqLinkRegisterEntity);
        assertThat(wqLinkRegisterEntity.getMaatId()).isEqualTo(courtDataDTO.getCaseDetails().getMaatId());
    }

    public String getSaveAndLinkString() {
        return """
            {
              "category": 12,
              "laaTransactionId":"e439dfc8-664e-4c8e-a999-d756dcf112c2",
              "caseUrn":"caseurn1",
              "asn": "123456754",
              "docLanguage": "EN",
              "caseCreationDate": "2019-08-16",
              "cjsAreaCode": "16",
              "createdUser": "testUser",
              "cjsLocation": "B16BG",
              "isActive" : true
            }""";
    }
}

