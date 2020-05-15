package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.SessionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SessionInfoProcessorTest {

    @InjectMocks
    private SessionInfoProcessor sessionInfoProcessor;
    @Spy
    private SessionRepository sessionRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<List<SessionEntity>> sessionsCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenSessionModel_whenProcessIsInvoked_thenSessionRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().get(0).getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(sessionsCaptor.getValue().get(0).getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(sessionsCaptor.getValue().get(0).getPostHearingCustody()).isEqualTo(caseDetails.getSessions().get(0).getPostHearingCustody());


    }
}
