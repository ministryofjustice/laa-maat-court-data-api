package gov.uk.courtdata.link.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_CREATION_EVENT;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.repository.WqCoreRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WqCoreInfoProcessorTest {

    @InjectMocks
    private WqCoreInfoProcessor wqCoreInfoProcessor;

    @Spy
    private WqCoreRepository wqCoreRepository;

    @Captor
    private ArgumentCaptor<WqCoreEntity> wqCoreCaptor;

    @Test
    void givenWQCoreModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();

        // when
        wqCoreInfoProcessor.process(courtDataDTO);

        // then
        verify(wqCoreRepository).save(wqCoreCaptor.capture());
        assertThat(wqCoreCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(wqCoreCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(wqCoreCaptor.getValue().getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);
        assertThat(wqCoreCaptor.getValue().getWqType()).isEqualTo(WQ_CREATION_EVENT);
    }
}
