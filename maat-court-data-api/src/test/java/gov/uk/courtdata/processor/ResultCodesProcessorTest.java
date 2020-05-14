package gov.uk.courtdata.processor;

import gov.uk.courtdata.entity.XLATResult;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResultCodesProcessorTest {

    @InjectMocks
    private ResultCodesProcessor resultCodesProcessor;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Spy
    private XLATResultRepository xlatResultRepository;

    @Captor
    private ArgumentCaptor<XLATResult> xlatResultArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testProcessResultCode_whenNewResultIsPassedIn_thenResultIsSaved() {

        //given
        Integer resultCode = 5555;

        //when
        Mockito.when(xlatResultRepository.findById(resultCode))
                .thenReturn(Optional.empty());

        resultCodesProcessor.processResultCode(resultCode);

        //then
        verify(xlatResultRepository).save(xlatResultArgumentCaptor.capture());
        assertThat(xlatResultArgumentCaptor.getValue().getCjsResultCode()).isEqualTo(5555);
        assertThat(xlatResultArgumentCaptor.getValue().getEnglandAndWales()).isEqualTo(YES);
        assertThat(xlatResultArgumentCaptor.getValue().getCreatedDate()).isEqualTo(LocalDate.now());
        assertThat(xlatResultArgumentCaptor.getValue().getResultDescription()).isEqualTo(RESULT_CODE_DESCRIPTION);
        assertThat(xlatResultArgumentCaptor.getValue().getCreatedUser()).isEqualTo(AUTO_USER);
        assertThat(xlatResultArgumentCaptor.getValue().getWqType()).isEqualTo(8);


    }

    @Test
    public void testProcessOffenceCode_whenOffenceExists_thenOffenceNotSaved() {

        //given
        Integer resultCode = 3333;
        final XLATResult xlatResult = XLATResult.builder().cjsResultCode(resultCode).build();

        //when
        Mockito.when(xlatResultRepository.findById(resultCode))
                .thenReturn(Optional.of(xlatResult));

        resultCodesProcessor.processResultCode(resultCode);

        //then
        verify(xlatResultRepository, times(0)).save(xlatResult);

    }

    @Test
    public void testProcessOffenceCode_whenNullCodeIsPassedIn_thenThrowsMaatCourtDataException() {

        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("A Null Result Code is passed in");
        resultCodesProcessor.processResultCode(null);
    }
}
