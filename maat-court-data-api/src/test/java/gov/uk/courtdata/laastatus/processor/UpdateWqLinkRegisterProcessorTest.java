package gov.uk.courtdata.laastatus.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateWqLinkRegisterProcessorTest {

    @InjectMocks
    private UpdateWqLinkRegisterProcessor wqLinkRegisterProcessor;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterCaptor;

    @Test
    void givenWQLinkRegisterModel_whenProcessIsInvoked_thenWQLinkRecordIsUpdated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = new ArrayList<>();
        wqLinkRegisterEntityList.add(
                WqLinkRegisterEntity.builder().maatId(1234).mlrCat(10).build());

        when(wqLinkRegisterRepository.findBymaatId(1234)).thenReturn(wqLinkRegisterEntityList);

        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(wqLinkRegisterCaptor.getValue().getMlrCat()).isEqualTo(caseDetails.getCategory());
    }
}
