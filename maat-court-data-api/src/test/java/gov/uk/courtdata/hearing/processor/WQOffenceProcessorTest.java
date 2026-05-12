package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.WQOffenceEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.HearingOffenceDTO;
import gov.uk.courtdata.repository.WQOffenceRepository;

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
class WQOffenceProcessorTest {

    @InjectMocks
    private WQOffenceProcessor wqOffenceProcessor;

    @Spy
    private WQOffenceRepository wqOffenceRepository;

    @Captor
    private ArgumentCaptor<WQOffenceEntity> wqOffenceEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenOffenceProcessor_whenProcessIsInvoke_thenSaveOffence1() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.getOffence().setOffenceWording("This is a short title");
        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());

        assertThat(wqOffenceEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("AP");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalaidReason()).isEqualTo("some aid reason");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getAsnSeq()).isEqualTo("001");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getOffenceWording()).isEqualTo("This is a short title");
    }

    @Test
    void givenOffenceProcessor_whenLAAStatusIsNull_thenSaveOffence() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(
                HearingOffenceDTO.builder().legalAidStatus(null).asnSeq("1").build());

        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("AP");
    }

    @Test
    void givenOffenceProcessor_whenLAAStatusIsRE_thenSaveOffence() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(
                HearingOffenceDTO.builder().legalAidStatus("RE").asnSeq("1").build());

        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("FB");
    }

    @Test
    void givenOffenceProcessor_whenLAAStatusIsVA_thenSaveOffence() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(
                HearingOffenceDTO.builder().legalAidStatus("VA").asnSeq("1").build());

        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("GR");
    }

    @Test
    void givenOffenceProcessor_whenLAAStatusIsWI_thenSaveOffence() {

        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(HearingOffenceDTO.builder()
                .legalAidStatus("WI")
                .applicationFlag(1)
                .asnSeq("1")
                .build());

        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("WD");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getApplicationFlag())
                .isEqualTo(1);
    }

    @Test
    void
            givenOffenceWordingIsGreaterThan4000Characters_whenProcessIsInvoked_thenSaveOffenceWithTruncatedOffenceWording() {
        // given
        String expectedOffenceWording = "a".repeat(CourtDataConstants.ORACLE_VARCHAR_MAX);
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();
        hearingDTO.getOffence().setOffenceWording("a".repeat(CourtDataConstants.ORACLE_VARCHAR_MAX + 1));

        // when
        wqOffenceProcessor.process(hearingDTO);

        // then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getOffenceWording()).isEqualTo(expectedOffenceWording);
    }
}
