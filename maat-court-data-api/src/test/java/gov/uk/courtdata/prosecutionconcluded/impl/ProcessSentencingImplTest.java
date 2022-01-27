package gov.uk.courtdata.prosecutionconcluded.impl;

import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static gov.uk.courtdata.enums.CrownCourtCaseType.APPEAL_CC;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessSentencingImplTest {

    @InjectMocks
    private ProcessSentencingImpl processSentencingImpl;

    @Mock
    private CrownCourtProcessingRepository crownCourtProcessingRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWhenAppealTypeCC_thenProcessInvoke() {

        doNothing().when(crownCourtProcessingRepository).invokeUpdateSentenceOrderDate(121121, null, LocalDate.parse("2012-12-12"));

        processSentencingImpl.processSentencingDate("2012-12-12",121121,"CC");

        verify(crownCourtProcessingRepository).invokeUpdateSentenceOrderDate(121121, null, LocalDate.parse("2012-12-12"));
    }

    @Test
    public void testWhenAppealTypeCC_thenProcessUpdate() {

        processSentencingImpl.processSentencingDate("2012-12-12",121121, APPEAL_CC.getValue());

        verify(crownCourtProcessingRepository).invokeUpdateAppealSentenceOrderDate(121121, null, LocalDate.parse("2012-12-12"),LocalDate.now());

    }
}