package gov.uk.courtdata.integration.link.service;


import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.link.service.CreateLinkCpJobStatusListener;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CreateLinkCpJobStatusServiceIntegrationTest extends MockMvcIntegrationTest {

    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private CreateLinkCpJobStatusListener createLinkCpJobStatusListener;

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

        Assertions.assertThrows(MAATCourtDataException.class,()->{
            //when
            createLinkCpJobStatusListener.receive(getMessageFromQueue());
        });


    }


    private String getMessageFromQueue() {

        return "{\n" +
                "  \"maatId\": 12345678,\n" +
                "  \"jobStatus\": \"SUCCESS\",\n" +
                "  \"laaTransactionId\": \"6f5b34ea-e038-4f1c-bfe5-d6bf622444f0\"\n" +
                "}";

    }
}
