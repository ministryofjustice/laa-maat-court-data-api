package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HardshipReviewServiceTest {

    @InjectMocks
    private HardshipReviewService hardshipReviewService;

    @Mock
    private HardshipReviewImpl hardshipReviewImpl;

    @Mock
    private HardshipReviewMapper hardshipReviewMapper;

    private static final int MOCK_REP_ID = 621580;
    private static final String MOCK_DETAIL_TYPE = "EXPENDITURE";
    private static final Integer MOCK_HARDSHIP_ID = 1000;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewImpl.find(any())).thenReturn(
                HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).build());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
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
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity))
                .thenReturn(HardshipReviewDTO.builder().id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build());

        HardshipReviewDTO returnedAssessment = hardshipReviewService.findHardshipReviewByRepId(MOCK_REP_ID);

        verify(hardshipReviewImpl).findByRepId(MOCK_REP_ID);
        verify(hardshipReviewMapper).hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
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
    public void whenFindHardshipReviewByDetailTypeIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReviewByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for Detail Type: %s and REP ID: %d", MOCK_DETAIL_TYPE, MOCK_REP_ID));
    }

    @Test
    public void whenFindHardshipReviewByDetailTypeIsInvoked_thenHardshipReviewIsRetrieved() {
        Optional<HardshipReviewEntity> hardshipReviewEntity = Optional.of(HardshipReviewEntity.builder()
                .id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build());
        when(hardshipReviewImpl.findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID)).thenReturn(hardshipReviewEntity);
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity.get()))
                .thenReturn(HardshipReviewDTO.builder().id(MOCK_HARDSHIP_ID).repId(MOCK_REP_ID).build());

        HardshipReviewDTO hardshipReview = hardshipReviewService.findHardshipReviewByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID);

        verify(hardshipReviewImpl).findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID);
        verify(hardshipReviewMapper).hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity.get());
        assertThat(hardshipReview.getId()).isEqualTo(MOCK_HARDSHIP_ID);
        assertThat(hardshipReview.getRepId()).isEqualTo(MOCK_REP_ID);
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.createHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        hardshipReviewService.createHardshipReview(CreateHardshipReview.builder().build());
        verify(hardshipReviewImpl).create(any());
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.updateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());

        hardshipReviewService.updateHardshipReview(UpdateHardshipReview.builder().build());
        verify(hardshipReviewImpl).update(any());
    }
}
