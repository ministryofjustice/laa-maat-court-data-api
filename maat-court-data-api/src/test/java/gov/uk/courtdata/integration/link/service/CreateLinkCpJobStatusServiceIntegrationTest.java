package gov.uk.courtdata.integration.link.service;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.link.service.CreateLinkCpJobStatusListener;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class CreateLinkCpJobStatusServiceIntegrationTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private CreateLinkCpJobStatusListener createLinkCpJobStatusListener;

    @Before
    public void setUp() {
        wqCoreRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();

    }

    @Test
    public void givenCpStatusJob_whenMessageIsReceived_thenCpStatusIsSuccess() {

        //given
        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder().maatId(12345678).createdTxId(88999).build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);

        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(wqLinkRegisterEntity.getCreatedTxId()).build();
        wqCoreRepository.save(wqCoreEntity);

        //when
        createLinkCpJobStatusListener.receive(getMessageFromQueue());

        //then
        Optional<WqCoreEntity> optionalWqCoreEntity = wqCoreRepository.findById(88999);

        optionalWqCoreEntity.ifPresent(coreEntity -> {
            assertThat(coreEntity.getTxId()).isEqualTo(88999);
            assertThat(coreEntity.getWqStatus()).isEqualTo(WQStatus.SUCCESS.value());
        });

    }


    @Test
    public void givenCpStatusJob_whenMessageIsReceived_thenThrowException() {

        //given
        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder().maatId(111111).createdTxId(88999).build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);

        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(wqLinkRegisterEntity.getCreatedTxId()).build();
        wqCoreRepository.save(wqCoreEntity);

        exceptionRule.expect(MAATCourtDataException.class);

        //when
        createLinkCpJobStatusListener.receive(getMessageFromQueue());
    }


    private String getMessageFromQueue() {

        return "{\n" +
                "  \"maatId\": 12345678,\n" +
                "  \"jobStatus\": \"SUCCESS\",\n" +
                "  \"laaTransactionId\": \"6f5b34ea-e038-4f1c-bfe5-d6bf622444f0\"\n" +
                "}";

    }
}
