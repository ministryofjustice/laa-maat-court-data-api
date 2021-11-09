package gov.uk.courtdata.integration.unlink.service;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.laastatus.client.CourtDataAdapterClient;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.unlink.service.UnlinkListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class UnlinkListenerTest {


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

    @Before
    public void setUp() {
        wqCoreRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        unlinkReasonRepository.deleteAll();
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
    }

    @Test
    public void givenUnlinkLinkJSONMessage_whenUnlinkListenerIsInvoked_thenCaseIsUnlinked() {

        //given
        Unlink unlink = gson.fromJson(testModelDataBuilder.getUnLinkString(), Unlink.class);
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
        WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
        RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderCPDataEntity();
        unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
        repOrderRepository.save(RepOrderEntity.builder().id(repOrderCPDataEntity.getRepOrderId()).caseId("12121").build());
        repOrderCPDataRepository.save(RepOrderCPDataEntity.builder()
                .defendantId("556677")
                .repOrderId(1234)
                .build());

        //when
        unlinkListener.receive(testModelDataBuilder.getUnLinkString());

        //then
        assertWQLinkRegister(unlinkModel);

    }


    private void assertWQLinkRegister(UnlinkModel unlinkModel) {

        List<WqLinkRegisterEntity> wqUnLinkRegisterEntity = wqLinkRegisterRepository.findAll();
        WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.get(0);
        assert unLinkRegister != null;
        Unlink unlink = unlinkModel.getUnlink();
        assertThat(unLinkRegister.getMaatId()).isEqualTo(1234);
        assertThat(unLinkRegister.getRemovedUserId()).isEqualTo(unlink.getUserId());
    }


}
