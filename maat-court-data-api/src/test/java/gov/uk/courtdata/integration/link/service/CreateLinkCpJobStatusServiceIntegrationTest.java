package gov.uk.courtdata.integration.link.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.link.service.CreateLinkCpJobStatusListener;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CreateLinkCpJobStatusServiceIntegrationTest extends MockMvcIntegrationTest {

    @Autowired
    private CreateLinkCpJobStatusListener createLinkCpJobStatusListener;

    @Test
    public void givenCpStatusJob_whenMessageIsReceived_thenCpStatusIsSuccess() {

        //given
        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder().maatId(12345678).createdTxId(88999).build();
      repos.wqLinkRegister.save(wqLinkRegisterEntity);

        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(wqLinkRegisterEntity.getCreatedTxId()).build();
      repos.wqCore.save(wqCoreEntity);

        //when
        createLinkCpJobStatusListener.receive(getMessageFromQueue());

        //then
      Optional<WqCoreEntity> optionalWqCoreEntity = repos.wqCore.findById(88999);

        optionalWqCoreEntity.ifPresent(coreEntity -> {
            assertThat(coreEntity.getTxId()).isEqualTo(88999);
            assertThat(coreEntity.getWqStatus()).isEqualTo(WQStatus.SUCCESS.value());
        });

    }


    @Test
    public void givenCpStatusJob_whenMessageIsReceived_thenThrowException() {

        //given
        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder().maatId(111111).createdTxId(88999).build();
      repos.wqLinkRegister.save(wqLinkRegisterEntity);

        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(wqLinkRegisterEntity.getCreatedTxId()).build();
      repos.wqCore.save(wqCoreEntity);

        Assertions.assertThrows(MAATCourtDataException.class,()->{
            //when
            createLinkCpJobStatusListener.receive(getMessageFromQueue());
        });


    }


    private String getMessageFromQueue() {

      return """
          {
            "maatId": 12345678,
            "jobStatus": "SUCCESS",
            "laaTransactionId": "6f5b34ea-e038-4f1c-bfe5-d6bf622444f0"
          }
          """;
    }
}
