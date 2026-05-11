package gov.uk.courtdata.link.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.PENDING_IOJ_DECISION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OffenceInfoProcessorTest {

    @InjectMocks
    private OffenceInfoProcessor offenceInfoProcessor;

    @Spy
    private OffenceRepository offenceRepository;

    @Captor
    private ArgumentCaptor<List<OffenceEntity>> offenceCaptor;

    @Test
    void givenOffenceDetails_whenProcessIsInvoked_thenOffenceRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        Offence offence =
                courtDataDTO.getCaseDetails().getDefendant().getOffences().getFirst();

        // when
        offenceInfoProcessor.process(courtDataDTO);

        // then
        verify(offenceRepository).saveAll(offenceCaptor.capture());
        OffenceEntity entity = offenceCaptor.getValue().getFirst();
        assertThat(entity.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(entity.getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(entity.getAsnSeq()).isEqualTo(offence.getAsnSeq());
        assertThat(entity.getOffenceWording()).isEqualTo(offence.getOffenceWording());
        assertThat(entity.getIojDecision()).isEqualTo(PENDING_IOJ_DECISION);
        assertThat(entity.getWqOffence()).isEqualTo(G_NO);
    }
}
