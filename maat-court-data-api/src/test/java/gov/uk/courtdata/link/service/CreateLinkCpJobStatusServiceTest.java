package gov.uk.courtdata.link.service;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JobStatus;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateLinkCpJobStatusServiceTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @InjectMocks
    private CreateLinkCpJobStatusService createLinkCpJobStatusService;
    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Spy
    private WqCoreRepository wqCoreRepository;
    @Captor
    private ArgumentCaptor<WqCoreEntity> wqCoreCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCpStatus_whenMessageFromQueueReceived_thenCpStatusSuccess() {

        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(88999).build();

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(Collections.singletonList(wqLinkRegisterEntity));

        when(wqCoreRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(WqCoreEntity.builder()
                        .txId(88999)
                        .build()));

        createLinkCpJobStatusService.execute(getCpJobStatus());

        verify(wqCoreRepository).save(wqCoreCaptor.capture());
        assertEquals(88999, wqCoreCaptor.getValue().getTxId());
        assertEquals(WQStatus.SUCCESS.value(), wqCoreCaptor.getValue().getWqStatus());
    }

    @Test
    public void givenCpStatusMessage_whenMaatIdNotExist_thenThrowException() {

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(new ArrayList<>());

        exceptionRule.expect(MAATCourtDataException.class);
        exceptionRule.expectMessage("No Record found for Maat ID - 12344455");

        createLinkCpJobStatusService.execute(getCpJobStatus());
    }

    @Test
    public void givenCpStatusMessage_whenCreatedTxIdNotExist_thenThrowException() {

        WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(88999)
                .maatId(12344455).build();

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(Collections.singletonList(wqLinkRegisterEntity));

        when(wqCoreRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        exceptionRule.expect(MAATCourtDataException.class);
        exceptionRule.expectMessage("No Record found for Maat ID- 12344455, Txn ID-88999");

        createLinkCpJobStatusService.execute(getCpJobStatus());
    }

    private CpJobStatus getCpJobStatus() {

        return CpJobStatus.builder()
                .maatId(12344455)
                .laaTransactionId(UUID.randomUUID())
                .jobStatus(JobStatus.SUCCESS)
                .build();
    }
}
