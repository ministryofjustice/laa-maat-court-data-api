package gov.uk.courtdata.integration.laastatus.service;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.laastatus.service.LaaStatusJobListener;
import gov.uk.courtdata.repository.WqCoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class CpJobStatusListenerIntegrationTest {

    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private LaaStatusJobListener laaStatusJobListener;

    @BeforeEach
    public void setUp() {
        wqCoreRepository.deleteAll();
    }

    @Test
    public void givenLAAStatusJOb_whenMessageIsReceived_thenStatusIsUpdated() {

        //given
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(123456).build();
        wqCoreRepository.save(wqCoreEntity);

        String laaStatusJobPayload = "{\n" +
                "  \"maatId\": 4136282,\n" +
                "  \"jobStatus\": \"SUCCESS\",\n" +
                "  \"laaTransactionId\": \"6f5b34ea-e038-4f1c-bfe5-d6bf622444f0\",\n" +
                "  \"laaStatusTransactionId\": 123456\n" +
                "}";

        //when
        laaStatusJobListener.receive(laaStatusJobPayload);

        //then
        Optional<WqCoreEntity> optionalWqCoreEntity = wqCoreRepository.findById(123456);

        optionalWqCoreEntity.ifPresent(wqCoreEntity1 -> {
            assertThat(wqCoreEntity1.getTxId()).isEqualTo(123456);
            assertThat(wqCoreEntity1.getWqStatus()).isEqualTo(WQStatus.SUCCESS.value());
        });

    }
}
