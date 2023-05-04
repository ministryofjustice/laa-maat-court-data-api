package gov.uk.courtdata.integration.unlink.service;

import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CourtHouseCodesEntity;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.unlink.service.UnlinkListener;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
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
        passportAssessmentRepository.deleteAll();
        financialAssessmentRepository.deleteAll();
        wqCoreRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        unlinkReasonRepository.deleteAll();
        repOrderRepository.deleteAll();
        repOrderCPDataRepository.deleteAll();
        queueMessageLogRepository.deleteAll();
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
        Map<String, Object> header = new HashMap<>();
        header.put("MessageId","AIDAIU3GACVJITZULQ2RQ");
        MessageHeaders headers = new MessageHeaders(header);
        unlinkListener.receive(testModelDataBuilder.getUnLinkString(), headers);


        //then
        assertWQLinkRegister(unlinkModel);

        queueMessageLogTestHelper.assertQueueMessageLogged(testModelDataBuilder.getUnLinkString(), 1, "e439dfc8-664e-4c8e-a999-d756dcf112c2", 1234);

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
