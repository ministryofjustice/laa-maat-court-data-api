package gov.uk.courtdata.laastatus.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.repository.DefendantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.constants.CourtDataConstants.SAVE_MLR;
import static gov.uk.courtdata.constants.CourtDataConstants.SEARCH_TYPE_0;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateDefendantInfoProcessorTest {


    @InjectMocks
    private UpdateDefendantInfoProcessor defendantInfoProcessor;
    @Spy
    private DefendantRepository defendantRepository;

    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    private ArgumentCaptor<DefendantEntity> defendantCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        assertThat(defendantCaptor.getValue().getDatasource()).isEqualTo(SAVE_MLR);
        assertThat(defendantCaptor.getValue().getSearchType()).isEqualTo(SEARCH_TYPE_0);
    }
}
