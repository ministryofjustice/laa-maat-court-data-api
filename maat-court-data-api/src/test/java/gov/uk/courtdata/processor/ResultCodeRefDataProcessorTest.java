package gov.uk.courtdata.processor;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.XLATResultRepository;
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
public class ResultCodeRefDataProcessorTest {

    @InjectMocks
    private ResultCodeRefDataProcessor resultCodeRefDataProcessor;

    @Spy
    private XLATResultRepository xlatResultRepository;

    @Captor
    private ArgumentCaptor<XLATResultEntity> xlatResultArgumentCaptor;


    @Test
    public void testProcessResultCode_whenNewResultIsPassedIn_thenResultIsSaved() {

        //given
        Integer resultCode = 5555;

        //when
        Mockito.when(xlatResultRepository.findById(resultCode))
                .thenReturn(Optional.empty());

        resultCodeRefDataProcessor.processResultCode(resultCode);

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
    public void testProcessResultCode_whenOffenceExists_thenOffenceNotSaved() {

        //given
        Integer resultCode = 3333;
        final XLATResultEntity xlatResultEntity = XLATResultEntity.builder().cjsResultCode(resultCode).build();

        //when
        Mockito.when(xlatResultRepository.findById(resultCode))
                .thenReturn(Optional.of(xlatResultEntity));

        resultCodeRefDataProcessor.processResultCode(resultCode);

        //then
        verify(xlatResultRepository, times(0)).save(xlatResultEntity);

    }

    @Test
    public void testProcessResultCode_whenNullCodeIsPassedIn_thenThrowsMaatCourtDataException() {

        Assertions.assertThrows(MAATCourtDataException.class, ()->{
            resultCodeRefDataProcessor.processResultCode(null);
        }, "A Null Result Code is passed in");

    }
}
