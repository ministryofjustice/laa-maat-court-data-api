package gov.uk.courtdata.processor;

import gov.uk.courtdata.entity.XLATOffenceEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.XLATOffenceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OffenceCodeRefDataProcessorTest {

    @InjectMocks
    private OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;

    @Spy
    private XLATOffenceRepository xlatOffenceRepository;

    @Captor
    private ArgumentCaptor<XLATOffenceEntity> offenceCodeCaptor;


    @Test
    public void testProcessOffenceCode_whenNewOffenceIsPassedIn_thenOffenceIsSaved() {

        //given
        String offenceCode = "ABCDEF";

        //when
        Mockito.when(xlatOffenceRepository.findById(offenceCode))
                .thenReturn(Optional.empty());

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

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
    public void testProcessOffenceCode_whenNewOffenceIsPassedInWithLessThan4Digit_thenOffenceIsSaved() {

        //given
        String offenceCode = "ABC";

        //when
        Mockito.when(xlatOffenceRepository.findById(offenceCode))
                .thenReturn(Optional.empty());

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

        //then
        verify(xlatOffenceRepository).save(offenceCodeCaptor.capture());
        assertThat(offenceCodeCaptor.getValue().getParentCode()).isEqualTo("ABC");


    }

    @Test
    public void testProcessOffenceCode_whenOffenceExists_thenOffenceNotSaved() {

        //given
        String offenceCode = "ABCD";
        final XLATOffenceEntity xlatOffenceEntity = XLATOffenceEntity.builder().offenceCode(offenceCode).build();

        //when
        Mockito.when(xlatOffenceRepository.findById(offenceCode))
                .thenReturn(Optional.of(xlatOffenceEntity));

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

        //then
        verify(xlatOffenceRepository, times(0)).save(xlatOffenceEntity);
    }

    @Test
    public void testProcessOffenceCode_whenNullCodeIsPassedIn_thenThrowsMaatCourtDataException() {

        Assertions.assertThrows(MAATCourtDataException.class,()->{
            offenceCodeRefDataProcessor.processOffenceCode(null);
        },"A Null Offence Code is passed in");

    }
}
