package gov.uk.courtdata.laastatus.impl;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laastatus.processor.UpdateDefendantInfoProcessor;
import gov.uk.courtdata.laastatus.processor.UpdateOffenceInfoProcessor;
import gov.uk.courtdata.laastatus.processor.UpdateWqCoreInfoProcessor;
import gov.uk.courtdata.laastatus.processor.UpdateWqLinkRegisterProcessor;
import gov.uk.courtdata.link.processor.CaseInfoProcessor;
import gov.uk.courtdata.link.processor.SessionInfoProcessor;
import gov.uk.courtdata.link.processor.SolicitorInfoProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
