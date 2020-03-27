package gov.uk.courtdata.integrationTest.unlink;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.UnlinkReasonRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.impl.UnLinkImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_UNLINK_EVENT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class UnLinkImplIntegrationTest {


    private UnLinkImpl unLinkImpl;
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    private WqCoreRepository wqCoreRepository;
    private UnlinkReasonRepository unlinkReasonRepository;
    private Gson gson;
    private TestModelDataBuilder testModelDataBuilder;
    private TestEntityDataBuilder testEntityDataBuilder;


    @Test
    @Ignore
    public void givenUnlinkLinkModel_whenUnlinkImplIsInvoked_thenCaseIsUnlinked() {

        //given
        Unlink unlink = gson.fromJson(testModelDataBuilder.getUnLinkString(), Unlink.class);
        UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
        WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
        RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderCPDataEntity();
        unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);

        //when
        unLinkImpl.execute(unlinkModel);

        //then
        assertWQLinkRegister(unlinkModel);
        assertWQCore(unlinkModel);
        assertUnLinkReason(unlinkModel);

    }


    private void assertWQLinkRegister(UnlinkModel unlinkModel) {

        Optional<WqLinkRegisterEntity> wqUnLinkRegisterEntity = wqLinkRegisterRepository.findById(unlinkModel.getWqLinkRegisterEntity().getCreatedTxId());
        WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.orElse(null);
        assert unLinkRegister != null;
        Unlink unlink = unlinkModel.getUnlink();
        assertThat(unLinkRegister.getMaatId()).isEqualTo(unlink.getMaatId());
        assertThat(unLinkRegister.getRemovedUserId()).isEqualTo(unlink.getUserId());
    }

    private void assertWQCore(UnlinkModel unlinkModel) {

        Optional<WqCoreEntity> wqCoreEntity = wqCoreRepository.findById(unlinkModel.getTxId());
        WqCoreEntity unLinkCore = wqCoreEntity.orElse(null);
        assert unLinkCore != null;
        assertThat(unLinkCore.getCaseId()).isEqualTo(unlinkModel.getWqLinkRegisterEntity().getCaseId());
        assertThat(unLinkCore.getTxId()).isEqualTo(unlinkModel.getTxId());
        assertThat(unLinkCore.getCreatedUserId()).isEqualTo(unlinkModel.getUnlink().getUserId());
        assertThat(unLinkCore.getWqType()).isEqualTo(WQ_UNLINK_EVENT);
        assertThat(unLinkCore.getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);

    }

    private void assertUnLinkReason(UnlinkModel unlinkModel) {
        Optional<UnlinkEntity> unlinkEntity = unlinkReasonRepository.findById(unlinkModel.getTxId());
        UnlinkEntity unLinkReason = unlinkEntity.orElse(null);
        assert unLinkReason != null;

        assertThat(unLinkReason.getTxId()).isEqualTo(unlinkModel.getTxId());
        assertThat(unLinkReason.getCaseId()).isEqualTo(unlinkModel.getWqLinkRegisterEntity().getCaseId());
        assertThat(unLinkReason.getReasonId()).isEqualTo(unlinkModel.getUnlink().getReasonId());
        assertThat(unLinkReason.getOtherReason()).isEqualTo(unlinkModel.getUnlink().getReasonText());
    }

    @Autowired
    public void setUnLinkImpl(UnLinkImpl unLinkImpl) {
        this.unLinkImpl = unLinkImpl;
    }
    @Autowired
    public void setWqLinkRegisterRepository(WqLinkRegisterRepository wqLinkRegisterRepository) {
        this.wqLinkRegisterRepository = wqLinkRegisterRepository;
    }
    @Autowired
    public void setWqCoreRepository(WqCoreRepository wqCoreRepository) {
        this.wqCoreRepository = wqCoreRepository;
    }
    @Autowired
    public void setUnlinkReasonRepository(UnlinkReasonRepository unlinkReasonRepository) {
        this.unlinkReasonRepository = unlinkReasonRepository;
    }
    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }
    @Autowired
    public void setTestModelDataBuilder(TestModelDataBuilder testModelDataBuilder) {
        this.testModelDataBuilder = testModelDataBuilder;
    }
    @Autowired
    public void setTestEntityDataBuilder(TestEntityDataBuilder testEntityDataBuilder) {
        this.testEntityDataBuilder = testEntityDataBuilder;
    }

}
