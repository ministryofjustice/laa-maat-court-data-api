package gov.uk.courtdata.link.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderInfoProcessorTest {

    @InjectMocks
    private RepOrderInfoProcessor repOrderInfoProcessor;

    @Spy
    private RepOrderRepository repOrderRepository;

    @Captor
    ArgumentCaptor<RepOrderEntity> repOrderCaptor;

    @Test
    void givenRepOrderData_whenProcessIsInvoked_thenRepOrderRecordIsUpdatedWitASNId() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        // when
        when(repOrderRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(
                        RepOrderEntity.builder().id(1234).arrestSummonsNo("asn").build()));

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
            assertThat(duration.getSeconds()).isZero();
            assertThat(rep.getUserModified()).isEqualTo("testUser");
        }
    }
}
