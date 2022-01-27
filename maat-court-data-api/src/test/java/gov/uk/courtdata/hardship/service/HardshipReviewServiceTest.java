package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.hardship.impl.HardshipReviewDetailImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewProgressImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

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
    private HardshipReviewDetailImpl hardshipReviewDetailImpl;

    @Mock
    private HardshipReviewProgressImpl hardshipReviewProgressImpl;

    @Mock
    private HardshipReviewMapper hardshipReviewMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewService.buildHardshipReviewDTO(any())).thenReturn(
                HardshipReviewDTO.builder().id(1000).build()
        );
        HardshipReviewDTO returnedAssessment = hardshipReviewService.find(1000);

        verify(hardshipReviewImpl).find(any());
        verify(hardshipReviewDetailImpl).find(any());
        verify(hardshipReviewProgressImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenBuildHardshipAssessmentDTOIsInvoked_thenDTOIsReturned() {
        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        HardshipReviewDetailEntity detailEntity = TestEntityDataBuilder.getHardshipReviewDetailsEntity();
        HardshipReviewProgressEntity progressEntity = TestEntityDataBuilder.getHardshipReviewProgressEntity();

        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(TestModelDataBuilder.getHardshipReviewDTO());
        when(hardshipReviewMapper.HardshipReviewDetailEntityToHardshipReviewDetail(any())).thenReturn(TestModelDataBuilder.getHardshipReviewDetail());
        when(hardshipReviewMapper.HardshipReviewProgressEntityToHardshipReviewProgress(any())).thenReturn(TestModelDataBuilder.getHardshipReviewProgress());

        HardshipReviewDTO expectedDTO = TestModelDataBuilder.getHardshipReviewDTOWithDetailsAndProgress();
        HardshipReviewDTO actualDTO = hardshipReviewService.buildHardshipReviewDTO(hardshipReview, List.of(detailEntity), List.of(progressEntity));

        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void whenBuildFinancialAssessmentDTOIsInvokedWithNoAssessmentDetails_thenDTOWithNoDetailsIsReturned() {
        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();

        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(TestModelDataBuilder.getHardshipReviewDTO());

        HardshipReviewDTO expectedDTO = TestModelDataBuilder.getHardshipReviewDTO();
        HardshipReviewDTO actualDTO = hardshipReviewService.buildHardshipReviewDTO(hardshipReview);

        assertThat(actualDTO).isEqualTo(expectedDTO);
        assertThat(actualDTO.getReviewDetails()).isNull();
    }
}
