package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.ProceedingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProceedingsInfoProcessorTest {

    @InjectMocks
    private ProceedingsInfoProcessor proceedingsInfoProcessor;
    @Spy
    private ProceedingRepository proceedingRepository;

    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    private ArgumentCaptor<ProceedingEntity> proceedingInfoCaptor;

    @BeforeEach
    public void setUp() {
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenProceedingDetails_whenProcessIsInvoked_thenProceedingRecordIsCreated() {
        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        // when
        proceedingsInfoProcessor.process(courtDataDTO);

        // then
        verify(proceedingRepository).save(proceedingInfoCaptor.capture());
        assertThat(proceedingInfoCaptor.getValue().getCreatedTxid()).isEqualTo(courtDataDTO.getTxId());
        assertThat(proceedingInfoCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(proceedingInfoCaptor.getValue().getProceedingId()).isEqualTo(courtDataDTO.getProceedingId());
        assertThat(proceedingInfoCaptor.getValue().getCreatedUser()).isEqualTo(caseDetails.getCreatedUser());

    }
}
