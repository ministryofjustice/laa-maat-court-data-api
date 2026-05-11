package gov.uk.courtdata.link.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.CREATE_LINK;
import static gov.uk.courtdata.constants.CourtDataConstants.SEARCH_TYPE_0;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.repository.DefendantRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefendantInfoProcessorTest {

    @InjectMocks
    private DefendantInfoProcessor defendantInfoProcessor;

    @Spy
    private DefendantRepository defendantRepository;

    @Captor
    private ArgumentCaptor<DefendantEntity> defendantCaptor;

    @Test
    void givenDefendantDetails_whenProcessIsInvoked_thenDefendantRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();

        // when
        defendantInfoProcessor.process(courtDataDTO);

        // then
        verify(defendantRepository).save(defendantCaptor.capture());
        assertThat(defendantCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(defendantCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(defendantCaptor.getValue().getDatasource()).isEqualTo(CREATE_LINK);
        assertThat(defendantCaptor.getValue().getSearchType()).isEqualTo(SEARCH_TYPE_0);
    }
}
