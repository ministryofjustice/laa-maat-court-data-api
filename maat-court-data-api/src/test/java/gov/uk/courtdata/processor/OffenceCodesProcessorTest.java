package gov.uk.courtdata.processor;

import gov.uk.courtdata.entity.XLATOffence;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.repository.XLATOffenceRepository;
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
public class OffenceCodesProcessorTest {

    @InjectMocks
    private OffenceCodesProcessor offenceCodesProcessor;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Spy
    private XLATOffenceRepository xlatOffenceRepository;

    @Captor
    private ArgumentCaptor<XLATOffence> offenceCodeCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testProcessOffenceCode_whenNewOffenceIsPassedIn_thenOffenceIsSaved() {

        //given
        String offenceCode = "ABCDEF";

        //when
        Mockito.when(xlatOffenceRepository.findById(offenceCode))
                .thenReturn(Optional.empty());

        offenceCodesProcessor.processOffenceCode(offenceCode);

        //then
        verify(xlatOffenceRepository).save(offenceCodeCaptor.capture());
        assertThat(offenceCodeCaptor.getValue().getOffenceCode()).isEqualTo(offenceCode);
        assertThat(offenceCodeCaptor.getValue().getParentCode()).isEqualTo("ABCD");
        assertThat(offenceCodeCaptor.getValue().getCreatedDate()).isEqualTo(LocalDate.now());
        assertThat(offenceCodeCaptor.getValue().getApplicationFlag()).isEqualTo(G_NO);
        assertThat(offenceCodeCaptor.getValue().getCodeMeaning()).isEqualTo(UNKNOWN_OFFENCE);
        assertThat(offenceCodeCaptor.getValue().getCreatedUser()).isEqualTo(AUTO_USER);
        assertThat(offenceCodeCaptor.getValue().getCodeStart()).isEqualTo(LocalDate.now());

    }

    @Test
    public void testProcessOffenceCode_whenOffenceExists_thenOffenceNotSaved() {

        //given
        String offenceCode = "ABCD";
        final XLATOffence xlatOffence = XLATOffence.builder().offenceCode(offenceCode).build();

        //when
        Mockito.when(xlatOffenceRepository.findById(offenceCode))
                .thenReturn(Optional.of(xlatOffence));

        offenceCodesProcessor.processOffenceCode(offenceCode);

        //then
        verify(xlatOffenceRepository, times(0)).save(xlatOffence);
    }

    @Test
    public void testProcessOffenceCode_whenNullCodeIsPassedIn_thenThrowsMaatCourtDataException() {

        exceptionRule.expect(MaatCourtDataException.class);
        exceptionRule.expectMessage("A Null Offence Code is passed in");
        offenceCodesProcessor.processOffenceCode(null);
    }
}
