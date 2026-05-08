package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.VerdictEntity;
import gov.uk.courtdata.enums.VerdictCategoryType;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.VerdictRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class VerdictProcessorTest {

    @InjectMocks
    private VerdictProcessor verdictProcessor;

    @Spy
    private VerdictRepository verdictRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<VerdictEntity> verdictEntityArgumentCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenVerdict() {
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTOForCCOutcome();

        // when
        verdictProcessor.process(hearingDTO);

        verify(verdictRepository).save(verdictEntityArgumentCaptor.capture());
        assertThat(verdictEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(789034);
        assertThat(verdictEntityArgumentCaptor.getValue().getVerdictCode()).isEqualTo("CD234");
        assertThat(verdictEntityArgumentCaptor.getValue().getVerdictDate()).isEqualTo("2020-10-21");
        assertThat(verdictEntityArgumentCaptor.getValue().getCategory()).isEqualTo("Verdict_Category");
        assertThat(verdictEntityArgumentCaptor.getValue().getCategoryType())
                .isEqualTo(VerdictCategoryType.GUILTY_CONVICTED.name());
        assertThat(verdictEntityArgumentCaptor.getValue().getCjsVerdictCode()).isEqualTo("88999");
    }
}
