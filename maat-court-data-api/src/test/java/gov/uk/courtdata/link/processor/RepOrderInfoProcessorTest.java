package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepOrderInfoProcessorTest {

    @InjectMocks
    private RepOrderInfoProcessor repOrderInfoProcessor;
    @Spy
    private RepOrderRepository repOrderRepository;

    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    ArgumentCaptor<RepOrderEntity> repOrderCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }


    @Test
    public void givenRepOrderData_whenProcessIsInvoked_thenRepOrderRecordIsUpdatedWitASNId() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        Defendant defendant = courtDataDTO.getCaseDetails().getDefendant();
        // when
        when(repOrderRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(RepOrderEntity
                        .builder()
                        .id(1234)
                        .arrestSummonsNo("asn")
                        .build()));

        repOrderInfoProcessor.process(courtDataDTO);


        // then
        verify(repOrderRepository).save(repOrderCaptor.capture());

        assertThat(repOrderCaptor.getValue().getCaseId()).isEqualTo("CP25467");
        assertThat(repOrderCaptor.getValue().getId()).isEqualTo(1234);
        assertThat(repOrderCaptor.getValue().getArrestSummonsNo()).isEqualTo("123456754");

        Optional<RepOrderEntity> repOrderEntity = repOrderRepository.findById(1234);
        if (repOrderEntity.isPresent()) {
            RepOrderEntity rep = repOrderEntity.get();
            Duration duration = Duration.between(rep.getDateModified(), LocalDateTime.now());
            assertThat(duration.getSeconds()).isEqualTo(0);
            assertThat(rep.getUserModified()).isEqualTo("testUser");

        }
    }
}
