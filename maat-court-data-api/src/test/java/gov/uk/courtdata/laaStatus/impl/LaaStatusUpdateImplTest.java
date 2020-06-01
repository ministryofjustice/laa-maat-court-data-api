package gov.uk.courtdata.laaStatus.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.processor.UpdateDefendantInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateOffenceInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqCoreInfoProcessor;
import gov.uk.courtdata.laaStatus.processor.UpdateWqLinkRegisterProcessor;
import gov.uk.courtdata.link.processor.CaseInfoProcessor;
import gov.uk.courtdata.link.processor.SessionInfoProcessor;
import gov.uk.courtdata.link.processor.SolicitorInfoProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LaaStatusUpdateImplTest {

    @InjectMocks
    private LaaStatusUpdateImpl laaStatusUpdateImpl;
    @Mock
    private IdentifierRepository identifierRepository;
    @Mock
    private CaseInfoProcessor caseInfoProcessor;
    @Mock
    private UpdateWqCoreInfoProcessor updateWqCoreInfoProcessor;
    @Mock
    private UpdateWqLinkRegisterProcessor updateWqLinkRegisterProcessor;
    @Mock
    private SolicitorInfoProcessor solicitorInfoProcessor;

    @Mock
    private UpdateDefendantInfoProcessor updateDefendantInfoProcessor;
    @Mock
    private SessionInfoProcessor sessionInfoProcessor;
    @Mock
    private UpdateOffenceInfoProcessor updateOffenceInfoProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenProcessors_whenImplIsInvoked_thenRequiredProcessorsAreInvoked() {
        //given
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().build();

        //when
        laaStatusUpdateImpl.execute(courtDataDTO);

        //then
        verify(caseInfoProcessor, times(1)).process(courtDataDTO);
        verify(updateWqCoreInfoProcessor, times(1)).process(courtDataDTO);
        verify(updateWqLinkRegisterProcessor, times(1)).process(courtDataDTO);
        verify(solicitorInfoProcessor, times(1)).process(courtDataDTO);
        verify(updateDefendantInfoProcessor, times(1)).process(courtDataDTO);
        verify(sessionInfoProcessor, times(1)).process(courtDataDTO);
        verify(updateOffenceInfoProcessor, times(1)).process(courtDataDTO);
    }
}
