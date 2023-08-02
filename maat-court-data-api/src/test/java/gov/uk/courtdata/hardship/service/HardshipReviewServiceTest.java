package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HardshipReviewServiceTest {

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
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewImpl.find(any())).thenReturn(
                HardshipReviewEntity.builder().id(MOCK_HARDSHIP_ID).build());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                HardshipReviewDTO.builder().id(MOCK_HARDSHIP_ID).build());

        HardshipReviewDTO returnedAssessment = hardshipReviewService.findHardshipReview(MOCK_HARDSHIP_ID);

        verify(hardshipReviewImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_HARDSHIP_ID);
    }

    @Test
    void whenFindIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.find(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReview(MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for ID: %d", MOCK_REP_ID));
    }

    @Test
    void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
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
    void whenFindByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.findByRepId(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReviewByRepId(MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for REP ID: %d", MOCK_REP_ID));
    }

    @Test
    void whenFindHardshipReviewByDetailTypeIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(hardshipReviewImpl.findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> hardshipReviewService.findHardshipReviewByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID))
                .withMessageContaining(String.format("No Hardship Review found for Detail Type: %s and REP ID: %d", MOCK_DETAIL_TYPE, MOCK_REP_ID));
    }

    @Test
    void whenFindHardshipReviewByDetailTypeIsInvoked_thenHardshipReviewIsRetrieved() {
        List<HardshipReviewDetailEntity> hardshipReviewDetailEntityList = List.of(HardshipReviewDetailEntity.builder()
                .id(MOCK_HARDSHIP_ID).build());
        when(hardshipReviewImpl.findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID)).thenReturn(hardshipReviewDetailEntityList);
        when(hardshipReviewMapper.hardshipReviewDetailEntityToHardshipReviewDetail(hardshipReviewDetailEntityList.get(0)))
                .thenReturn(HardshipReviewDetail.builder().id(MOCK_HARDSHIP_ID).build());

        List<HardshipReviewDetail> hardshipReviewDetailList = hardshipReviewService.findHardshipReviewByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID);

        verify(hardshipReviewImpl).findByDetailType(MOCK_DETAIL_TYPE, MOCK_REP_ID);
        verify(hardshipReviewMapper).hardshipReviewDetailEntityToHardshipReviewDetail(hardshipReviewDetailEntityList.get(0));
        assertThat(hardshipReviewDetailList.get(0).getId()).isEqualTo(MOCK_HARDSHIP_ID);
    }

    @Test
    void whenCreateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.createHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        hardshipReviewService.createHardshipReview(CreateHardshipReview.builder().build());
        verify(hardshipReviewImpl).create(any());
    }

    @Test
    void whenUpdateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.updateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO());

        hardshipReviewService.updateHardshipReview(UpdateHardshipReview.builder().build());
        verify(hardshipReviewImpl).update(any());
    }
}
