package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.hearing.dto.HearingDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static gov.uk.courtdata.enums.JurisdictionType.CROWN;
import static gov.uk.courtdata.enums.JurisdictionType.MAGISTRATES;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HearingWQProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
    @Mock
    private WQHearingProcessor wqHearingProcessor;

    @Mock
    private PleaProcessor pleaProcessor;
    @Mock
    private VerdictProcessor verdictProcessor;

    @Mock
    private LinkRegisterProcessor linkRegisterProcessor;

    @InjectMocks
    private HearingWQProcessor hearingWQProcessor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

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
        verify(wqHearingProcessor,times(1)).process(hearingDTO);
        verify(linkRegisterProcessor,times(1)).process(hearingDTO);
        verify(pleaProcessor,times(1)).process(hearingDTO);
        verify(verdictProcessor,times(1)).process(hearingDTO);
        verify(linkRegisterProcessor,times(1)).process(hearingDTO);

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
        verify(wqHearingProcessor).process(hearingDTO);
    }
}
