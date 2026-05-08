package gov.uk.courtdata.link.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.link.processor.CaseInfoProcessor;
import gov.uk.courtdata.link.processor.DefendantInfoProcessor;
import gov.uk.courtdata.link.processor.OffenceInfoProcessor;
import gov.uk.courtdata.link.processor.ProceedingsInfoProcessor;
import gov.uk.courtdata.link.processor.RepOrderCPInfoProcessor;
import gov.uk.courtdata.link.processor.RepOrderInfoProcessor;
import gov.uk.courtdata.link.processor.ResultsInfoProcessor;
import gov.uk.courtdata.link.processor.SessionInfoProcessor;
import gov.uk.courtdata.link.processor.SolicitorInfoProcessor;
import gov.uk.courtdata.link.processor.WqCoreInfoProcessor;
import gov.uk.courtdata.link.processor.WqLinkRegisterProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SaveAndLinkImplTest {

    @InjectMocks
    private SaveAndLinkImpl saveAndLink;

    @Mock
    private CaseInfoProcessor caseInfoProcessor;

    @Mock
    private WqCoreInfoProcessor wqCoreInfoProcessor;

    @Mock
    private WqLinkRegisterProcessor wqLinkRegisterProcessor;

    @Mock
    private SolicitorInfoProcessor solicitorInfoProcessor;

    @Mock
    private ProceedingsInfoProcessor proceedingsInfoProcessor;

    @Mock
    private DefendantInfoProcessor defendantInfoProcessor;

    @Mock
    private SessionInfoProcessor sessionInfoProcessor;

    @Mock
    private OffenceInfoProcessor offenceInfoProcessor;

    @Mock
    private ResultsInfoProcessor resultsInfoProcessor;

    @Mock
    private RepOrderCPInfoProcessor repOrderCPInfoProcessor;

    @Mock
    private IdentifierRepository identifierRepository;

    @Mock
    private RepOrderInfoProcessor repOrderInfoProcessor;

    @Test
    public void givenCaseDetails_whenExecuteIsInvoked_thenCaseIsLinked() {

        // given
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().build();

        // when
        when(identifierRepository.getCaseID()).thenReturn(1);
        when(identifierRepository.getTxnID()).thenReturn(1);
        when(identifierRepository.getProceedingID()).thenReturn(1);
        when(identifierRepository.getLibraID()).thenReturn(1);

        saveAndLink.execute(courtDataDTO);

        // then
        verify(caseInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(wqCoreInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(wqLinkRegisterProcessor, atLeastOnce()).process(courtDataDTO);
        verify(solicitorInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(proceedingsInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(defendantInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(sessionInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(offenceInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(resultsInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(repOrderCPInfoProcessor, atLeastOnce()).process(courtDataDTO);
        verify(repOrderInfoProcessor, atLeastOnce()).process(courtDataDTO);

        assertThat(courtDataDTO.getCaseId()).isEqualTo(1);
        assertThat(courtDataDTO.getProceedingId()).isEqualTo(1);
        assertThat(courtDataDTO.getTxId()).isEqualTo(1);
        assertThat(courtDataDTO.getLibraId()).isEqualTo("CP1");
    }
}
