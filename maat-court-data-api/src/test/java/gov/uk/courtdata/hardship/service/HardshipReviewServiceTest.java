package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HardshipReviewServiceTest {

    @InjectMocks
    private HardshipReviewService hardshipReviewService;

    @Mock
    private HardshipReviewImpl hardshipReviewImpl;

    @Mock
    private HardshipReviewMapper hardshipReviewMapper;

    private static final int MOCK_REP_ID = 621580;
    private static final Integer MOCK_HARDSHIP_ID = 1000;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewImpl.find(any())).thenReturn(
                HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).build());
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                HardshipReviewDTO.builder().id(MOCK_HARDSHIP_ID).build());

        HardshipReviewDTO returnedAssessment = hardshipReviewService.findHardshipReview(MOCK_HARDSHIP_ID);

        verify(hardshipReviewImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_HARDSHIP_ID);
    }

    @Test
    public void whenFindIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.find(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReview(MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for ID: %d", MOCK_REP_ID));
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        HardshipReviewEntity hardshipReviewEntity = HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build();
        when(hardshipReviewImpl.findByRepId(MOCK_REP_ID)).thenReturn(hardshipReviewEntity);
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity))
                .thenReturn(HardshipReviewDTO.builder().id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build());

        HardshipReviewDTO returnedAssessment = hardshipReviewService.findHardshipReviewByRepId(MOCK_REP_ID);

        verify(hardshipReviewImpl).findByRepId(MOCK_REP_ID);
        verify(hardshipReviewMapper).HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_HARDSHIP_ID);
        assertThat(returnedAssessment.getRepId()).isEqualTo(MOCK_REP_ID);
    }

    @Test
    public void whenFindByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.findByRepId(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReviewByRepId(MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for REP ID: %d", MOCK_REP_ID));
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.CreateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        hardshipReviewService.createHardshipReview(CreateHardshipReview.builder().build());
        verify(hardshipReviewImpl).create(any());
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.UpdateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());

        hardshipReviewService.updateHardshipReview(UpdateHardshipReview.builder().build());
        verify(hardshipReviewImpl).update(any());
    }
}
