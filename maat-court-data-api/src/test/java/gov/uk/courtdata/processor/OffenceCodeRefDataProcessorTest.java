package gov.uk.courtdata.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.AUTO_USER;
import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.constants.CourtDataConstants.UNKNOWN_OFFENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.entity.XLATOffenceEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.XLATOffenceRepository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OffenceCodeRefDataProcessorTest {

    @InjectMocks
    private OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;

    @Spy
    private XLATOffenceRepository xlatOffenceRepository;

    @Captor
    private ArgumentCaptor<XLATOffenceEntity> offenceCodeCaptor;

    @Test
    void testProcessOffenceCode_whenNewOffenceIsPassedIn_thenOffenceIsSaved() {

        // given
        String offenceCode = "ABCDEF";

        // when
        Mockito.when(xlatOffenceRepository.findById(offenceCode)).thenReturn(Optional.empty());

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

        // then
        verify(xlatOffenceRepository).save(offenceCodeCaptor.capture());
        XLATOffenceEntity entity = offenceCodeCaptor.getValue();
        assertThat(entity.getOffenceCode()).isEqualTo(offenceCode);
        assertThat(entity.getParentCode()).isEqualTo("ABCD");
        assertThat(entity.getCreatedDate()).isEqualTo(LocalDate.now());
        assertThat(entity.getApplicationFlag()).isEqualTo(G_NO);
        assertThat(entity.getCodeMeaning()).isEqualTo(UNKNOWN_OFFENCE);
        assertThat(entity.getCreatedUser()).isEqualTo(AUTO_USER);
        assertThat(entity.getCodeStart()).isEqualTo(LocalDate.now());
    }

    @Test
    void testProcessOffenceCode_whenNewOffenceIsPassedInWithLessThan4Digit_thenOffenceIsSaved() {

        // given
        String offenceCode = "ABC";

        // when
        Mockito.when(xlatOffenceRepository.findById(offenceCode)).thenReturn(Optional.empty());

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

        // then
        verify(xlatOffenceRepository).save(offenceCodeCaptor.capture());
        assertThat(offenceCodeCaptor.getValue().getParentCode()).isEqualTo("ABC");
    }

    @Test
    void testProcessOffenceCode_whenOffenceExists_thenOffenceNotSaved() {

        // given
        String offenceCode = "ABCD";
        final XLATOffenceEntity xlatOffenceEntity =
                XLATOffenceEntity.builder().offenceCode(offenceCode).build();

        // when
        Mockito.when(xlatOffenceRepository.findById(offenceCode)).thenReturn(Optional.of(xlatOffenceEntity));

        offenceCodeRefDataProcessor.processOffenceCode(offenceCode);

        // then
        verify(xlatOffenceRepository, never()).save(xlatOffenceEntity);
    }

    @Test
    void testProcessOffenceCode_whenNullCodeIsPassedIn_thenThrowsMaatCourtDataException() {

        assertThatThrownBy(() -> offenceCodeRefDataProcessor.processOffenceCode(null))
                .isInstanceOf(MAATCourtDataException.class)
                .hasMessage("A Null Offence Code is passed in");
    }
}
