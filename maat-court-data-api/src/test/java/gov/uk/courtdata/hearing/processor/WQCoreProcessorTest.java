package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WQCoreProcessorTest {

    @InjectMocks
    private WQCoreProcessor wqCoreProcessor;

    @Spy
    private WqCoreRepository wqCoreRepository;
    @Spy
    private XLATResultRepository xlatResultRepository;
    @Spy
    private OffenceRepository offenceRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<WqCoreEntity> argumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }
    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {
        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();

        //when
        when(offenceRepository.getOffenceCountForAsnSeq(Mockito.anyInt(),Mockito.anyString())).thenReturn(123);

        XLATResultEntity xlatResultEntity = XLATResultEntity.builder().wqType(1).notes("some notes").build();
        Optional<XLATResultEntity> resultEntity = Optional.of(xlatResultEntity);
        when(xlatResultRepository.findById(Mockito.anyInt())).thenReturn(resultEntity);

        wqCoreProcessor.process(hearingDTO);

        //then
        verify(wqCoreRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(argumentCaptor.getValue().getExtendedProcessing()).isEqualTo(99);
        assertThat(argumentCaptor.getValue().getWqType()).isEqualTo(1);
    }
 }
