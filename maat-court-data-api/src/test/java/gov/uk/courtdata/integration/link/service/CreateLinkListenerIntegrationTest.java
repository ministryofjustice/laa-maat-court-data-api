package gov.uk.courtdata.integration.link.service;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CourtHouseCodesEntity;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.link.service.CreateLinkListener;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CreateLinkListenerIntegrationTest {


    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private RepOrderCPDataRepository repOrderDataRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;
    @Autowired
    private CourtHouseCodesRepository courtHouseCodesRepository;
    @Autowired
    private CreateLinkListener createLinkListener;
    @Autowired
    private SolicitorMAATDataRepository solicitorMAATDataRepository;
    @Autowired
    private DefendantMAATDataRepository defendantMAATDataRepository;
    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;
    @Autowired
    private QueueMessageLogTestHelper queueMessageLogTestHelper;
    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;
    @Autowired
    private PassportAssessmentRepository passportAssessmentRepository;

    @BeforeEach
    public void setUp() {
        new RepositoryUtil().clearUp(financialAssessmentRepository,
                passportAssessmentRepository,
                repOrderRepository,
                wqLinkRegisterRepository,
                repOrderDataRepository,
                solicitorMAATDataRepository,
                defendantMAATDataRepository,
                courtHouseCodesRepository,
                queueMessageLogRepository);
    }

    @Test
    public void givenSaveAndLinkModel_whenSaveAndImplIsInvoked_thenLinkEstablished() {

        //given
        repOrderDataRepository.save(testEntityDataBuilder.getRepOrderEntity());
        repOrderRepository.save(TestEntityDataBuilder.getRepOrder());
        courtHouseCodesRepository.save(CourtHouseCodesEntity.builder().code("B16BG").effectiveDateFrom(LocalDateTime.now()).build());
        solicitorMAATDataRepository.save(testEntityDataBuilder.getSolicitorMAATDataEntity());
        defendantMAATDataRepository.save(testEntityDataBuilder.getDefendantMAATDataEntity());

        String saveAndLinkMessage = testModelDataBuilder.getSaveAndLinkString();

        //when
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId", "AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        //when
        createLinkListener.receive(saveAndLinkMessage, headers);

        //then
        CourtDataDTO courtDataDTO = testModelDataBuilder.getSaveAndLinkModelRaw();

        verifyWqLinkRegister(courtDataDTO);
        verifyRepOrder(courtDataDTO);

        queueMessageLogTestHelper.assertQueueMessageLogged(saveAndLinkMessage, 1, "e439dfc8-664e-4c8e-a999-d756dcf112c2", 1234);

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
        assertThat(wqLinkRegisterRepository.findAll().size()).isEqualTo(0);
    }

    private void verifyRepOrder(CourtDataDTO courtDataDTO) {
        // Verify CP Rep Order Record is created
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Optional<RepOrderCPDataEntity> retrievedRepOrderEntity = repOrderDataRepository.findByrepOrderId(caseDetails.getMaatId());
        RepOrderCPDataEntity found = retrievedRepOrderEntity.orElse(null);
        assert found != null;
        assertThat(found.getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
        assertThat(found.getRepOrderId()).isEqualTo(caseDetails.getMaatId());
        assertThat(found.getDefendantId()).isEqualTo(caseDetails.getDefendant().getDefendantId());
    }


    private void verifyWqLinkRegister(CourtDataDTO courtDataDTO) {
        // Verify WQCore Link register Record is created
        List<WqLinkRegisterEntity> retrievedWqLinkRegisterEntity = wqLinkRegisterRepository.findBymaatId(courtDataDTO.getCaseDetails().getMaatId());
        WqLinkRegisterEntity wqLinkRegisterEntity = retrievedWqLinkRegisterEntity.get(0);
        assert wqLinkRegisterEntity != null;
        assertThat(wqLinkRegisterEntity.getMaatId()).isEqualTo(courtDataDTO.getCaseDetails().getMaatId());
    }
    public String getSaveAndLinkString() {
        return "{\n" +

                "  \"category\": 12,\n" +
                "  \"laaTransactionId\":\"e439dfc8-664e-4c8e-a999-d756dcf112c2\",\n" +
                "  \"caseUrn\":\"caseurn1\",\n" +
                "  \"asn\": \"123456754\",\n" +
                "  \"docLanguage\": \"EN\",\n" +
                "  \"caseCreationDate\": \"2019-08-16\",\n" +
                "  \"cjsAreaCode\": \"16\",\n" +
                "  \"createdUser\": \"testUser\",\n" +
                "  \"cjsLocation\": \"B16BG\",\n" +
                "  \"isActive\" : true\n" +
                "}";
    }
}

