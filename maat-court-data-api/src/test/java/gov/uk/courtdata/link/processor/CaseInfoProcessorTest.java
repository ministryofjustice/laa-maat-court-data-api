package gov.uk.courtdata.link.processor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.CaseRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CaseInfoProcessorTest {

    @InjectMocks
    private CaseInfoProcessor caseInfoProcessor;

    @Spy
    private CaseRepository caseRepository;

    @Captor
    private ArgumentCaptor<CaseEntity> caseInfoCaptor;

    @Test
    void givenCaseDetails_whenProcessIsInvoked_theCaseRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        // when
        caseInfoProcessor.process(courtDataDTO);

        // then
        verify(caseRepository).save(caseInfoCaptor.capture());
        assertThat(caseInfoCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(caseInfoCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(caseInfoCaptor.getValue().getDocLanguage()).isEqualTo(caseDetails.getDocLanguage());
        assertThat(caseInfoCaptor.getValue().getInactive()).isEqualTo("N");
        assertThat(caseInfoCaptor.getValue().getProceedingId()).isEqualTo(courtDataDTO.getProceedingId());
    }

    @Test
    void givenCaseDetailsWithINActiveANDNullDate_whenProcessIsInvoked_theCaseRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCaseCreationDate(null);
        caseDetails.setActive(false);

        // when
        caseInfoProcessor.process(courtDataDTO);

        // then
        verify(caseRepository).save(caseInfoCaptor.capture());

        assertThat(caseInfoCaptor.getValue().getInactive()).isEqualTo("Y");
    }

    @Test
    void givenCaseDetailsWithSingleDigitCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeISProcessed() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCaseCreationDate(null);
        caseDetails.setActive(false);
        caseDetails.setCjsAreaCode("5");

        // when
        caseInfoProcessor.process(courtDataDTO);

        // then
        verify(caseRepository).save(caseInfoCaptor.capture());

        assertThat(caseInfoCaptor.getValue().getInactive()).isEqualTo("Y");
        assertThat(caseInfoCaptor.getValue().getCjsAreaCode()).isEqualTo("05");
    }

    @Test
    void givenCaseDetailsWithTowDigitCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeISProcessed() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCjsAreaCode("16");

        // when
        caseInfoProcessor.process(courtDataDTO);

        // then
        verify(caseRepository).save(caseInfoCaptor.capture());
        assertThat(caseInfoCaptor.getValue().getCjsAreaCode()).isEqualTo("16");
    }
}
