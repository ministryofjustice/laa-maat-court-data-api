package gov.uk.courtdata.unlink;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.UnlinkReasonRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class UnLinkProcessorTest {

    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
    @Autowired
    private TestEntityDataBuilder testEntityDataBuilder;
    @Autowired
    private UnLinkProcessor unlinkProcessor;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private UnlinkReasonRepository unlinkReasonRepository;
    @Autowired
    private Gson gson;



    @Test
    public void givenUnlinkLinkModel_whenUnlinkProcessorIsInvoked_thenCaseIsUnlinked() {

        //given
        String unLinkJson = testModelDataBuilder.getUnLinkString();
        WqLinkRegisterEntity wqLinkRegisterEntity = testEntityDataBuilder.getWqLinkRegisterEntity();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);

        //when
       UnlinkModel unlinkModel =  unlinkProcessor.process(gson.fromJson(unLinkJson, Unlink.class));

        //then
        verifyWQUnLinkRegister(unlinkModel);
        verifyWQCore(unlinkModel);
        verifyUnlinkReason(unlinkModel);

    }

    private void verifyWQUnLinkRegister(UnlinkModel unlinkModel) {
        Optional<WqLinkRegisterEntity> wqUnLinkRegisterEntity = wqLinkRegisterRepository.findById(unlinkModel.getWqLinkRegisterEntity().getCreatedTxId());
        WqLinkRegisterEntity unLinkRegister = wqUnLinkRegisterEntity.orElse(null);
        assert unLinkRegister != null;
    }

    private void verifyWQCore(UnlinkModel unlinkModel) {
        Optional<WqCoreEntity> wqCoreEntity = wqCoreRepository.findById(unlinkModel.getTxId());
        WqCoreEntity unLinkCore = wqCoreEntity.orElse(null);
        assert unLinkCore != null;
    }

    private void verifyUnlinkReason(UnlinkModel unlinkModel) {
        Optional<UnlinkEntity> unlinkEntity = unlinkReasonRepository.findById(unlinkModel.getTxId());
        UnlinkEntity unLinkReason = unlinkEntity.orElse(null);
        assert unLinkReason != null;
    }
}
