package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQDefendantProcessorTest {

    @InjectMocks
    private WQDefendantProcessor wqDefendantProcessor;

    @Spy
    private WQDefendantRepository defendantRepository;

    @Captor
    private ArgumentCaptor<WQDefendant> wqDefendantArgumentCaptor;

    @Test
    void givenDefendantProcessor_whenProcessIsInvoke_thenSaveDefendant() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        // when
        wqDefendantProcessor.process(hearingDTO);

        // then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());
        assertThat(wqDefendantArgumentCaptor.getValue().getSurname()).isEqualTo("Smith");
        assertThat(wqDefendantArgumentCaptor.getValue().getPostCode()).isEqualTo("LU3 111");
    }
}
