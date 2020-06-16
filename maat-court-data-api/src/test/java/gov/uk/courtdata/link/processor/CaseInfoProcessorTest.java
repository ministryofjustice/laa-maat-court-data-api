package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.CaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CaseInfoProcessorTest {


    @InjectMocks
    private CaseInfoProcessor caseInfoProcessor;

    @Spy
    private CaseRepository caseRepository;
    private TestModelDataBuilder testModelDataBuilder;
    @Captor
    private ArgumentCaptor<CaseEntity> caseInfoCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseDetails_whenProcessIsInvoked_theCaseRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        //when
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
    public void givenCaseDetailsWithINActiveANDNullDate_whenProcessIsInvoked_theCaseRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        final CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.setCaseCreationDate(null);
        caseDetails.setActive(false);

        //when
        caseInfoProcessor.process(courtDataDTO);

        // then
        verify(caseRepository).save(caseInfoCaptor.capture());

        assertThat(caseInfoCaptor.getValue().getInactive()).isEqualTo("Y");

    }
}