package gov.uk.courtdata.link.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.ProceedingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProceedingsInfoProcessorTest {

    @InjectMocks
    private ProceedingsInfoProcessor proceedingsInfoProcessor;

    @Spy
    private ProceedingRepository proceedingRepository;

    @Captor
    private ArgumentCaptor<ProceedingEntity> proceedingInfoCaptor;

    @Test
    void givenProceedingDetails_whenProcessIsInvoked_thenProceedingRecordIsCreated() {
        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        // when
        proceedingsInfoProcessor.process(courtDataDTO);

        // then
        verify(proceedingRepository).save(proceedingInfoCaptor.capture());
        assertThat(proceedingInfoCaptor.getValue().getCreatedTxid()).isEqualTo(courtDataDTO.getTxId());
        assertThat(proceedingInfoCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(proceedingInfoCaptor.getValue().getProceedingId()).isEqualTo(courtDataDTO.getProceedingId());
        assertThat(proceedingInfoCaptor.getValue().getCreatedUser()).isEqualTo(caseDetails.getCreatedUser());
    }
}
