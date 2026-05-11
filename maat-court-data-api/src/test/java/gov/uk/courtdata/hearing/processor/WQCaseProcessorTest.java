package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQCaseEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQCaseRepository;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQCaseProcessorTest {

    @InjectMocks
    private WQCaseProcessor wqCaseProcessor;

    @Spy
    private WQCaseRepository wqCaseRepository;

    @Captor
    private ArgumentCaptor<WQCaseEntity> wqCaseEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        // when
        wqCaseProcessor.process(hearingDTO);

        // then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getDocLanguage()).isEqualTo("en");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
    }

    @Test
    void givenCaseProcessor_whenNullCJSCodeIsPassedIN_thenSaveNullCJSCode() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setCjsAreaCode(null);

        // when
        wqCaseProcessor.process(hearingDTO);

        // then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isNull();
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getProceedingId()).isEqualTo(9999);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getLibraCreationDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void givenCaseProcessor_whenCreationDateAvailable_thenSaveCase() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setCaseCreationDate("2020-05-20");

        // when
        wqCaseProcessor.process(hearingDTO);

        // then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getDocLanguage()).isEqualTo("en");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getLibraCreationDate()).isEqualTo("2020-05-20");
    }
}
