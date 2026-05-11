package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.XLATResultRepository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQCoreProcessorTest {

    @InjectMocks
    private WQCoreProcessor wqCoreProcessor;

    @Spy
    private WqCoreRepository wqCoreRepository;

    @Spy
    private XLATResultRepository xlatResultRepository;

    @Spy
    private OffenceRepository offenceRepository;

    @Captor
    ArgumentCaptor<WqCoreEntity> argumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        // when
        when(offenceRepository.getOffenceCountForAsnSeq(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(123);

        XLATResultEntity xlatResultEntity =
                XLATResultEntity.builder().wqType(1).notes("some notes").build();
        Optional<XLATResultEntity> resultEntity = Optional.of(xlatResultEntity);
        when(xlatResultRepository.findById(Mockito.anyInt())).thenReturn(resultEntity);

        wqCoreProcessor.process(hearingDTO);

        // then
        verify(wqCoreRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(argumentCaptor.getValue().getExtendedProcessing()).isEqualTo(99);
        assertThat(argumentCaptor.getValue().getWqType()).isEqualTo(1);
    }
}
