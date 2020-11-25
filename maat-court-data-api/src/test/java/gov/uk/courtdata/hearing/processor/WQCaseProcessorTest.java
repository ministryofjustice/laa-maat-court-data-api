package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQCaseEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQCaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQCaseProcessorTest {

    @InjectMocks
    private WQCaseProcessor wqCaseProcessor;

    @Spy
    private WQCaseRepository wqCaseRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WQCaseEntity> wqCaseEntityArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();

        //when
        wqCaseProcessor.process(hearingDTO);

        //then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getDocLanguage()).isEqualTo("en");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
    }

    @Test
    public void givenCaseProcessor_whenNullCJSCodeIsPassedIN_thenSaveNullCJSCode() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setCjsAreaCode(null);

        //when
        wqCaseProcessor.process(hearingDTO);

        //then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isEqualTo(null);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getProceedingId()).isEqualTo(9999);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getLibraCreationDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void givenCaseProcessor_whenCreationDateAvailable_thenSaveCase() {

        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();
        hearingDTO.setCaseCreationDate("2020-05-20");

        //when
        wqCaseProcessor.process(hearingDTO);

        //then
        verify(wqCaseRepository).save(wqCaseEntityArgumentCaptor.capture());
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getDocLanguage()).isEqualTo("en");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
        assertThat(wqCaseEntityArgumentCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
        assertThat(wqCaseEntityArgumentCaptor.getValue().getLibraCreationDate()).isEqualTo("2020-05-20");
    }
}