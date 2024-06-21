package gov.uk.courtdata.integration.unlink.impl;

import static gov.uk.courtdata.constants.CourtDataConstants.SYSTEM_UNLINKED;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_UNLINK_EVENT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.unlink.impl.UnLinkImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class UnLinkImplIntegrationTest extends MockMvcIntegrationTest {

  @Autowired
  private UnLinkImpl unLinkImpl;

  @Autowired
  private Gson gson;

  @Autowired
  private TestModelDataBuilder testModelDataBuilder;

  @Autowired
  private TestEntityDataBuilder testEntityDataBuilder;

  @Test
  public void givenUnlinkLinkModel_whenUnlinkImplIsInvoked_thenCaseIsUnlinked() {

    //given
    Unlink unlink = gson.fromJson(testModelDataBuilder.getUnLinkString(), Unlink.class);
    UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
    WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
    unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
    RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderCPDataEntity();
    unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
    repos.wqLinkRegister.save(wqLinkRegisterEntity);

    //when
    unLinkImpl.execute(unlinkModel);

    //then
    assertWQLinkRegister(unlinkModel);
    assertWQCore(unlinkModel);
    assertUnLinkReason(unlinkModel);

  }


  @Test
  public void givenUnlinkReasonIsOther_whenUnlinked_ThenOtherReasonTextIs() {

    Unlink unlink = gson.fromJson(testModelDataBuilder.getUnLinkWithOtherReasonString(),
        Unlink.class);
    UnlinkModel unlinkModel = UnlinkModel.builder().unlink(unlink).build();
    WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
    unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntity);
    RepOrderCPDataEntity repOrderCPDataEntity = testEntityDataBuilder.getRepOrderCPDataEntity();
    unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity);
    repos.wqLinkRegister.save(wqLinkRegisterEntity);

    unLinkImpl.execute(unlinkModel);

    assertWQLinkRegister(unlinkModel);
    assertWQCore(unlinkModel);
    assertUnLinkOtherReason(unlinkModel);

  }


  private void assertWQLinkRegister(UnlinkModel unlinkModel) {

    Optional<WqLinkRegisterEntity> wqUnLinkRegisterEntity = repos.wqLinkRegister.findById(
        unlinkModel.getWqLinkRegisterEntity().getCreatedTxId());
    WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.orElse(null);
    assertNotNull(unLinkRegister);
    Unlink unlink = unlinkModel.getUnlink();
    assertThat(unLinkRegister.getMaatId()).isEqualTo(unlink.getMaatId());
    assertThat(unLinkRegister.getRemovedUserId()).isEqualTo(unlink.getUserId());
  }

  private void assertWQCore(UnlinkModel unlinkModel) {

    Optional<WqCoreEntity> wqCoreEntity = repos.wqCore.findById(unlinkModel.getTxId());
    WqCoreEntity unLinkCore = wqCoreEntity.orElse(null);
    assertNotNull(unLinkCore);
    assertThat(unLinkCore.getCaseId()).isEqualTo(unlinkModel.getWqLinkRegisterEntity().getCaseId());
    assertThat(unLinkCore.getTxId()).isEqualTo(unlinkModel.getTxId());
    assertThat(unLinkCore.getCreatedUserId()).isEqualTo(unlinkModel.getUnlink().getUserId());
    assertThat(unLinkCore.getWqType()).isEqualTo(WQ_UNLINK_EVENT);
    assertThat(unLinkCore.getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);

  }

  private void assertUnLinkReason(UnlinkModel unlinkModel) {
    Optional<UnlinkEntity> unlinkEntity = repos.unlinkReason.findById(unlinkModel.getTxId());
    UnlinkEntity unLinkReason = unlinkEntity.orElse(null);
    assertNotNull(unLinkReason);

    assertThat(unLinkReason.getTxId()).isEqualTo(unlinkModel.getTxId());
    assertThat(unLinkReason.getCaseId()).isEqualTo(
        unlinkModel.getWqLinkRegisterEntity().getCaseId());
    assertThat(unLinkReason.getReasonId()).isEqualTo(unlinkModel.getUnlink().getReasonId());
    assertThat(unLinkReason.getOtherReason()).isEqualTo(SYSTEM_UNLINKED);
  }

  private void assertUnLinkOtherReason(UnlinkModel unlinkModel) {
    Optional<UnlinkEntity> unlinkEntity = repos.unlinkReason.findById(unlinkModel.getTxId());
    UnlinkEntity unLinkReason = unlinkEntity.orElse(null);
    assertNotNull(unLinkReason);

    assertThat(unLinkReason.getTxId()).isEqualTo(unlinkModel.getTxId());
    assertThat(unLinkReason.getCaseId()).isEqualTo(
        unlinkModel.getWqLinkRegisterEntity().getCaseId());
    assertThat(unLinkReason.getReasonId()).isEqualTo(unlinkModel.getUnlink().getReasonId());
    assertThat(unLinkReason.getOtherReason()).isEqualTo(
        unlinkModel.getUnlink().getOtherReasonText());
  }


}
