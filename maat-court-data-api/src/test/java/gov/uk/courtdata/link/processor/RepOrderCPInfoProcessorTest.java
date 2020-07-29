package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepOrderCPInfoProcessorTest {

    @Captor
    ArgumentCaptor<RepOrderCPDataEntity> repOrderCaptor;
    @InjectMocks
    private RepOrderCPInfoProcessor repOrderCPInfoProcessor;
    @Spy
    private RepOrderCPDataRepository repOrderDataRepository;
    private TestModelDataBuilder testModelDataBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }


    @Test
    public void givenRepOrderData_whenProcessIsInvoked_thenRepOrderRecordIsUpdatedWithDefendantId() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        Defendant defendant = courtDataDTO.getCaseDetails().getDefendant();
        // when
        when(repOrderDataRepository.findByrepOrderId(Mockito.anyInt()))
                .thenReturn(Optional.of(RepOrderCPDataEntity.builder().repOrderId(123).build()));
        repOrderCPInfoProcessor.process(courtDataDTO);


        // then
        verify(repOrderDataRepository).save(repOrderCaptor.capture());
        assertThat(repOrderCaptor.getValue().getCaseUrn()).isEqualTo("caseurn1");
        assertThat(repOrderCaptor.getValue().getRepOrderId()).isEqualTo(123);
        assertThat(repOrderCaptor.getValue().getDefendantId()).isEqualTo(defendant.getDefendantId());
        assertThat(repOrderCaptor.getValue().getUserModified()).isEqualTo("testUser");
        assertThat(repOrderCaptor.getValue().getDateModified()).isNotNull()
                .isExactlyInstanceOf(LocalDateTime.class);

        Optional<RepOrderCPDataEntity> rep = repOrderDataRepository.findByrepOrderId(123);
        if (rep.isPresent()) {
            final RepOrderCPDataEntity repOrderCPDataEntity = rep.get();
            assertThat(repOrderCPDataEntity.getCaseUrn()).isEqualTo("caseurn1");
        }
    }
}
