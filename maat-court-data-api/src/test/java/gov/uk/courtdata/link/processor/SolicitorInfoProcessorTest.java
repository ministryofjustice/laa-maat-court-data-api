package gov.uk.courtdata.link.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.repository.SolicitorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SolicitorInfoProcessorTest {

    @InjectMocks
    private SolicitorInfoProcessor solicitorInfoProcessor;

    @Spy
    private SolicitorRepository solicitorRepository;

    @Captor
    private ArgumentCaptor<SolicitorEntity> solicitorCaptor;

    @Test
    void givenSaveAndLinkModel_whenProcessIsInvoked_thenSolicitorRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();

        // when
        solicitorInfoProcessor.process(courtDataDTO);

        // then
        verify(solicitorRepository).save(solicitorCaptor.capture());
        assertThat(solicitorCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(solicitorCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(solicitorCaptor.getValue().getLaaOfficeAccount())
                .isEqualTo(solicitorMAATDataEntity.getAccountCode());
        assertThat(solicitorCaptor.getValue().getFirmName()).isEqualTo(solicitorMAATDataEntity.getAccountName());
    }
}
