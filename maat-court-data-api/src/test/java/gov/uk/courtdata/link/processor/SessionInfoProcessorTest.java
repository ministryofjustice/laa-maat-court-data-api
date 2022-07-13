package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.SessionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static gov.uk.courtdata.constants.CourtDataConstants.DEFAULT_HEARING_CUS_STATUS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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

    @Test
    public void givenSessionModelWithNullPostHearingCustody_whenProcessIsInvoked_thenSessionRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.getSessions().get(0).setPostHearingCustody(null);

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().get(0).getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(sessionsCaptor.getValue().get(0).getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(sessionsCaptor.getValue().get(0).getPostHearingCustody()).isEqualTo(DEFAULT_HEARING_CUS_STATUS);


    }

    @Test
    public void givenSessionModelWithNullHearingDate_whenProcessIsInvoked_thenSessionRecordIsNullFiltered() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.getSessions().get(0).setPostHearingCustody(null);

        courtDataDTO.getCaseDetails().getSessions().add(Session.builder().build());
        courtDataDTO.getCaseDetails().getSessions().add(Session.builder().courtLocation("B30PG").dateOfHearing(null).build());
        courtDataDTO.getCaseDetails().getSessions().add(Session.builder().courtLocation("B30PG").dateOfHearing("2021-04-30").build());

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().get(0).getCourtLocation()).isEqualTo("B16BG");
        assertThat(sessionsCaptor.getValue().size()).isEqualTo(2);
    }
}
