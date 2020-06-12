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

    @InjectMocks
    private HearingWQProcessor hearingWQProcessor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process(){

        HearingDTO hearingDTO = HearingDTO.builder().maatId(1212).build();

        hearingWQProcessor.process(hearingDTO);

        ///then
        verify(wqCaseProcessor,times(1)).process(hearingDTO);
        verify(wqCoreProcessor,times(1)).process(hearingDTO);
        verify(wqDefendantProcessor,times(1)).process(hearingDTO);
        verify(wqOffenceProcessor,times(1)).process(hearingDTO);
        verify(wqResultProcessor,times(1)).process(hearingDTO);
        verify(wqSessionProcessor,times(1)).process(hearingDTO);
    }
}
