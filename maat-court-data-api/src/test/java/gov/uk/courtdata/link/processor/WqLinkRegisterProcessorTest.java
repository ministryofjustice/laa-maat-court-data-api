package gov.uk.courtdata.link.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WqLinkRegisterProcessorTest {

    @InjectMocks
    private WqLinkRegisterProcessor wqLinkRegisterProcessor;

    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterCaptor;

    @Test
    void givenWQLinkRegisterModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();

        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getCreatedTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(wqLinkRegisterCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(wqLinkRegisterCaptor.getValue().getMaatCat()).isEqualTo(solicitorMAATDataEntity.getCmuId());
        assertThat(wqLinkRegisterCaptor.getValue().getMlrCat()).isEqualTo(solicitorMAATDataEntity.getCmuId());
        assertThat(wqLinkRegisterCaptor.getValue().getLibraId()).isEqualTo(courtDataDTO.getLibraId());
        assertThat(wqLinkRegisterCaptor.getValue().getMaatId()).isEqualTo(caseDetails.getMaatId());
        assertThat(wqLinkRegisterCaptor.getValue().getCaseUrn()).isEqualTo(caseDetails.getCaseUrn());
    }

    @Test
    void givenWQLinkRegisterModelWithSingleCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeIsProcessed() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCjsAreaCode("5");
        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
    }

    @Test
    void givenWQLinkRegisterModelWithTwoDigitCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeIsProcessed() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCjsAreaCode("16");
        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getCjsAreaCode()).isEqualTo("16");
    }
}
