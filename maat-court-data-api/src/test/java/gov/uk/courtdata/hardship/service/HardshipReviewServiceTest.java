package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
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

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewImpl.find(any())).thenReturn(
                HardshipReviewEntity.builder().id(1000).build()
        );
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                HardshipReviewDTO.builder().id(1000).build()
        );
        HardshipReviewDTO returnedAssessment = hardshipReviewService.findHardshipReview(1000);

        verify(hardshipReviewImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.CreateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO()
        );
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO()
        );
        hardshipReviewService.createHardshipReview(CreateHardshipReview.builder().build());
        verify(hardshipReviewImpl).create(any());
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsPersisted() {
        when(hardshipReviewMapper.UpdateHardshipReviewToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO()
        );
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                TestModelDataBuilder.getHardshipReviewDTO()
        );
        hardshipReviewService.updateHardshipReview(UpdateHardshipReview.builder().build());
        verify(hardshipReviewImpl).update(any());
    }
}
