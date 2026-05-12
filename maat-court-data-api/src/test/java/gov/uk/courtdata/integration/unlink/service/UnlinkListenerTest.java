package gov.uk.courtdata.integration.unlink.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.unlink.service.UnlinkListener;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;

import com.google.gson.Gson;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class UnlinkListenerTest extends MockMvcIntegrationTest {

    @Autowired
    private Gson gson;

    @Autowired
    private CourtDataAdapterClient courtDataAdapterClient;

    @Autowired
    private UnlinkListener unlinkListener;

    @Autowired
    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    @Test
    void givenUnlinkLinkJSONMessage_whenUnlinkListenerIsInvoked_thenCaseIsUnlinked() {

        // given

        RepOrderEntity repOrderEntity = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());

        Unlink unlink = gson.fromJson(TestModelDataBuilder.getUnLinkString(repOrderEntity.getId()), Unlink.class);
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
        WqLinkRegisterEntity wqLinkRegisterEntity =
                TestEntityDataBuilder.getWqLinkRegisterEntity(repOrderEntity.getId());
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
        RepOrderCPDataEntity repOrderCPDataEntity =
                TestEntityDataBuilder.getRepOrderCPDataEntity(repOrderEntity.getId());
        unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
        repos.wqLinkRegister.save(wqLinkRegisterEntity);
        repos.repOrderCPData.save(RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .repOrderId(repOrderEntity.getId())
                .build());

        // when
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId", "AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        unlinkListener.receive(TestModelDataBuilder.getUnLinkString(repOrderEntity.getId()), headers);

        // then
        assertWQLinkRegister(unlinkModel, repOrderEntity.getId());

        queueMessageLogTestHelper.assertQueueMessageLogged(
                TestModelDataBuilder.getUnLinkString(repOrderEntity.getId()),
                1,
                "e439dfc8-664e-4c8e-a999-d756dcf112c2",
                repOrderEntity.getId());
    }

    private void assertWQLinkRegister(UnlinkModel unlinkModel, Integer repId) {

        List<WqLinkRegisterEntity> wqUnLinkRegisterEntity = repos.wqLinkRegister.findAll();
        WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.getFirst();
        assertThat(unLinkRegister).isNotNull();
        Unlink unlink = unlinkModel.getUnlink();
        assertThat(unLinkRegister.getMaatId()).isEqualTo(repId);
        assertThat(unLinkRegister.getRemovedUserId()).isEqualTo(unlink.getUserId());
    }
}
