package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMON_PLATFORM;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WqLinkRegisterProcessorTest {

    @InjectMocks
    private WqLinkRegisterProcessor wqLinkRegisterProcessor;
    @Spy
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WqLinkRegisterEntity> wqLinkRegisterCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenWQLinkRegisterModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
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
    }

    @Test
    public void givenWQLinkRegisterModelWithSingleCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeIsProcessed() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();
        caseDetails.setCjsAreaCode("5");
        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getCjsAreaCode()).isEqualTo("05");

    }

    @Test
    public void givenWQLinkRegisterModelWithTwoDigitCJSCode_whenProcessIsInvoked_thenTwoDigitCJSCodeIsProcessed() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        SolicitorMAATDataEntity solicitorMAATDataEntity = courtDataDTO.getSolicitorMAATDataEntity();
        caseDetails.setCjsAreaCode("16");
        // when
        wqLinkRegisterProcessor.process(courtDataDTO);

        // then
        verify(wqLinkRegisterRepository).save(wqLinkRegisterCaptor.capture());
        assertThat(wqLinkRegisterCaptor.getValue().getCjsAreaCode()).isEqualTo("16");

    }
}
