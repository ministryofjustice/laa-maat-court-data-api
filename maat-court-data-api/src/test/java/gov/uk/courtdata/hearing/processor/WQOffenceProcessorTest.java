package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.WQOffenceEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.HearingOffenceDTO;
import gov.uk.courtdata.repository.WQOffenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WQOffenceProcessorTest {

    @InjectMocks
    private WQOffenceProcessor wqOffenceProcessor;

    @Spy
    private WQOffenceRepository wqOffenceRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WQOffenceEntity> wqOffenceEntityArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenOffenceProcessor_whenProcessIsInvoke_thenSaveOffence1() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.getOffence().setOffenceWording("This is a short title");
        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());

        assertThat(wqOffenceEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("AP");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalaidReason()).isEqualTo("some aid reason");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getAsnSeq()).isEqualTo("001");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getOffenceWording()).isEqualTo("This is a short title");
    }

    @Test
    public void givenOffenceProcessor_whenLAAStatusIsNull_thenSaveOffence() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(HearingOffenceDTO.builder().legalAidStatus(null).asnSeq("1").build());

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("AP");

    }
    @Test
    public void givenOffenceProcessor_whenLAAStatusIsRE_thenSaveOffence() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(HearingOffenceDTO.builder().legalAidStatus("RE").asnSeq("1").build());

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("FB");
    }

    @Test
    public void givenOffenceProcessor_whenLAAStatusIsVA_thenSaveOffence() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(HearingOffenceDTO.builder().legalAidStatus("VA").asnSeq("1").build());

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("GR");
    }

    @Test
    public void givenOffenceProcessor_whenLAAStatusIsWI_thenSaveOffence() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setOffence(HearingOffenceDTO.builder().legalAidStatus("WI").applicationFlag(1).asnSeq("1").build());

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("WD");
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getApplicationFlag()).isEqualTo(1);
    }

    @Test
    public void givenOffenceWordingIsGreaterThan4000Characters_whenProcessIsInvoked_thenSaveOffenceWithTruncatedOffenceWording() {
        //given
        String expectedOffenceWording = "a".repeat(CourtDataConstants.ORACLE_VARCHAR_MAX);
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.getOffence().setOffenceWording("a".repeat(CourtDataConstants.ORACLE_VARCHAR_MAX + 1));

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());
        assertThat(wqOffenceEntityArgumentCaptor.getValue().getOffenceWording()).isEqualTo(expectedOffenceWording);
    }
}
