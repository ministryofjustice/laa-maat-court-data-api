package gov.uk.courtdata.integration.unlink.service;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.unlink.service.UnlinkListener;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class UnlinkListenerTest extends MockMvcIntegrationTest {


    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private UnlinkReasonRepository unlinkReasonRepository;
    @Autowired
    private Gson gson;
    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;
    @Autowired
    private CourtDataAdapterClient courtDataAdapterClient;
    @Autowired
    private UnlinkListener unlinkListener;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private RepOrderCPDataRepository repOrderCPDataRepository;
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
        new RepositoryUtil().clearUp(passportAssessmentRepository,
                financialAssessmentRepository,
                wqCoreRepository,
                wqLinkRegisterRepository,
                unlinkReasonRepository,
                repOrderRepository,
                repOrderCPDataRepository,
                queueMessageLogRepository);
    }

    @Test
    public void givenUnlinkLinkJSONMessage_whenUnlinkListenerIsInvoked_thenCaseIsUnlinked() {

        //given

        RepOrderEntity repOrderEntity = repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder());

        Unlink unlink = gson.fromJson(testModelDataBuilder.getUnLinkString(repOrderEntity.getId()), Unlink.class);
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
        WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity(repOrderEntity.getId());
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
        RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderCPDataEntity(repOrderEntity.getId());
        unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
        repOrderCPDataRepository.save(RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .repOrderId(repOrderEntity.getId())
                .build());

        //when
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId", "AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        unlinkListener.receive(testModelDataBuilder.getUnLinkString(repOrderEntity.getId()), headers);


        //then
        assertWQLinkRegister(unlinkModel, repOrderEntity.getId());

        queueMessageLogTestHelper.assertQueueMessageLogged(testModelDataBuilder.getUnLinkString(repOrderEntity.getId()), 1, "e439dfc8-664e-4c8e-a999-d756dcf112c2", repOrderEntity.getId());

    }


    private void assertWQLinkRegister(UnlinkModel unlinkModel, Integer repId) {

        List<WqLinkRegisterEntity> wqUnLinkRegisterEntity = wqLinkRegisterRepository.findAll();
        WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.get(0);
        assert unLinkRegister != null;
        Unlink unlink = unlinkModel.getUnlink();
        assertThat(unLinkRegister.getMaatId()).isEqualTo(repId);
        assertThat(unLinkRegister.getRemovedUserId()).isEqualTo(unlink.getUserId());
    }


}
