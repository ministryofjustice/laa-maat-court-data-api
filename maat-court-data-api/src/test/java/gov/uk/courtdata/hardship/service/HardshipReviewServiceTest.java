package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.hardship.impl.HardshipReviewDetailImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewProgressImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
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
    private HardshipReviewDetailImpl hardshipReviewDetailImpl;

    @Mock
    private HardshipReviewProgressImpl hardshipReviewProgressImpl;

    @Mock
    private HardshipReviewMapper hardshipReviewMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(any())).thenReturn(
                HardshipReviewDTO.builder().id(1000).build()
        );
        HardshipReviewDTO returnedAssessment = hardshipReviewService.find(1000);

        verify(hardshipReviewImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }
}
