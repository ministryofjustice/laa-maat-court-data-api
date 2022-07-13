package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.enums.JobStatus;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.repository.WqCoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CpJobStatusServiceTest {

    @InjectMocks
    private LaaStatusJobService laaStatusJobService;
    @Spy
    private WqCoreRepository wqCoreRepository;

    @Captor
    private ArgumentCaptor<WqCoreEntity> wqCoreCaptor;

    @Test
    public void givenStatusJobIsReceivedWithSUCCESSStatus_whenExecuteIsInvoked_thenStatusIsUpdated() {

        // given
        CpJobStatus cpJobStatus = CpJobStatus.builder()
                .laaStatusTransactionId(123456)
                .laaTransactionId(UUID.fromString("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0"))
                .jobStatus(JobStatus.SUCCESS)
                .maatId(1234)
                .build();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(123456).build();

        //when
        when(wqCoreRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(wqCoreEntity));

        laaStatusJobService.execute(cpJobStatus);

        //then
        verify(wqCoreRepository).save(wqCoreCaptor.capture());
        assertThat(wqCoreCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCoreCaptor.getValue().getWqStatus()).isEqualTo(WQStatus.SUCCESS.value());
    }



    @Test
    public void givenStatusJobIsReceivedWithFAILStatus_whenExecuteIsInvoked_thenStatusIsUpdated() {

        // given
        CpJobStatus cpJobStatus = CpJobStatus.builder()
                .laaStatusTransactionId(123456)
                .laaTransactionId(UUID.fromString("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0"))
                .jobStatus(JobStatus.FAIL)
                .maatId(1234)
                .build();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder().txId(123456).build();

        //when
        when(wqCoreRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(wqCoreEntity));

        laaStatusJobService.execute(cpJobStatus);

        //then
        verify(wqCoreRepository).save(wqCoreCaptor.capture());
        assertThat(wqCoreCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCoreCaptor.getValue().getWqStatus()).isEqualTo(WQStatus.FAIL.value());
    }

    @Test
    public void givenStatusJobIsReceivedWithSUCCESSStatusANDNoLAARecord_whenExecuteIsInvoked_thenExceptionISThrown() {

        // given
        CpJobStatus cpJobStatus = CpJobStatus.builder()
                .laaStatusTransactionId(123456)
                .laaTransactionId(UUID.fromString("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0"))
                .jobStatus(JobStatus.FAIL)
                .maatId(1234)
                .build();


        //when
        when(wqCoreRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(MAATCourtDataException.class, ()->{
            laaStatusJobService.execute(cpJobStatus);
        }, "No Record found for Maat ID- 1234, Txn ID-123456");



    }

}
