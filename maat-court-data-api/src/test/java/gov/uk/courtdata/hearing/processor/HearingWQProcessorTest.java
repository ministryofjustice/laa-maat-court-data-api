package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.hearing.dto.HearingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static gov.uk.courtdata.enums.JurisdictionType.CROWN;
import static gov.uk.courtdata.enums.JurisdictionType.MAGISTRATES;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HearingWQProcessorTest {

    @Mock
    private WQCaseProcessor wqCaseProcessor;
    @Mock
    private WQCoreProcessor wqCoreProcessor;
    @Mock
    private WQDefendantProcessor wqDefendantProcessor;
    @Mock
    private WQOffenceProcessor wqOffenceProcessor;
    @Mock
    private WQResultProcessor wqResultProcessor;
    @Mock
    private WQSessionProcessor wqSessionProcessor;

    @InjectMocks
    private HearingWQProcessor hearingWQProcessor;

    @Test
    public void givenHearingProcessor_whenProcessIsInvoke_thenProcessForCrown(){

        HearingDTO hearingDTO = HearingDTO
                .builder()
                .hearingId(UUID.randomUUID())
                .maatId(1212)
                .jurisdictionType(CROWN)
                .build();

        hearingWQProcessor.process(hearingDTO);

        ///then
        verify(wqCaseProcessor,times(1)).process(hearingDTO);
        verify(wqCoreProcessor,times(1)).process(hearingDTO);
        verify(wqDefendantProcessor,times(1)).process(hearingDTO);
        verify(wqOffenceProcessor,times(1)).process(hearingDTO);
        verify(wqResultProcessor,times(1)).process(hearingDTO);
        verify(wqSessionProcessor,times(1)).process(hearingDTO);
    }

    @Test
    public void givenHearingProcessor_whenProcessIsInvoke_thenProcessForMagistrates(){

        HearingDTO hearingDTO = HearingDTO
                .builder()
                .hearingId(UUID.randomUUID())
                .maatId(1212)
                .jurisdictionType(MAGISTRATES)
                .build();

        hearingWQProcessor.process(hearingDTO);

        ///then
        verify(wqCaseProcessor).process(hearingDTO);
        verify(wqCoreProcessor).process(hearingDTO);
        verify(wqDefendantProcessor).process(hearingDTO);
        verify(wqOffenceProcessor).process(hearingDTO);
        verify(wqResultProcessor).process(hearingDTO);
        verify(wqSessionProcessor).process(hearingDTO);
    }
}
