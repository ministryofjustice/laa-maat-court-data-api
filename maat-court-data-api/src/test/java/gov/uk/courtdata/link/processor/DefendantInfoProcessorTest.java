package gov.uk.courtdata.link.processor;


import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.repository.DefendantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static gov.uk.courtdata.constants.CourtDataConstants.CREATE_LINK;
import static gov.uk.courtdata.constants.CourtDataConstants.SEARCH_TYPE_0;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefendantInfoProcessorTest {

    @InjectMocks
    private DefendantInfoProcessor defendantInfoProcessor;
    @Spy
    private DefendantRepository defendantRepository;

    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    private ArgumentCaptor<DefendantEntity> defendantCaptor;

    @BeforeEach
    public void setUp() {
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenDefendantDetails_whenProcessIsInvoked_thenDefendantRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();

        //when
        defendantInfoProcessor.process(courtDataDTO);


        // then
        verify(defendantRepository).save(defendantCaptor.capture());
        assertThat(defendantCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(defendantCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(defendantCaptor.getValue().getDatasource()).isEqualTo(CREATE_LINK);
        assertThat(defendantCaptor.getValue().getSearchType()).isEqualTo(SEARCH_TYPE_0);

    }
}
