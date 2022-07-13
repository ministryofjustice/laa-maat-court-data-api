package gov.uk.courtdata.link.processor;


import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.PENDING_IOJ_DECISION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OffenceInfoProcessorTest {

    @InjectMocks
    private OffenceInfoProcessor offenceInfoProcessor;
    @Spy
    private OffenceRepository offenceRepository;

    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    private ArgumentCaptor<List<OffenceEntity>> OffenceCaptor;

    @BeforeEach
    public void setUp() {
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenOffenceDetails_whenProcessIsInvoked_thenOffenceRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        Offence offence = courtDataDTO.getCaseDetails().getDefendant().getOffences().get(0);

        //when
        offenceInfoProcessor.process(courtDataDTO);

        // then
        verify(offenceRepository).saveAll(OffenceCaptor.capture());
        assertThat(OffenceCaptor.getValue().get(0).getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(OffenceCaptor.getValue().get(0).getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(OffenceCaptor.getValue().get(0).getAsnSeq()).isEqualTo(offence.getAsnSeq());
        assertThat(OffenceCaptor.getValue().get(0).getOffenceWording()).isEqualTo(offence.getOffenceWording());
        assertThat(OffenceCaptor.getValue().get(0).getIojDecision()).isEqualTo(PENDING_IOJ_DECISION);
        assertThat(OffenceCaptor.getValue().get(0).getWqOffence()).isEqualTo(G_NO);

    }
}
