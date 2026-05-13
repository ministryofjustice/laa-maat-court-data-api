package gov.uk.courtdata.link.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.DEFAULT_HEARING_CUS_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.SessionRepository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionInfoProcessorTest {

    @InjectMocks
    private SessionInfoProcessor sessionInfoProcessor;

    @Spy
    private SessionRepository sessionRepository;

    @Captor
    private ArgumentCaptor<List<SessionEntity>> sessionsCaptor;

    @Test
    void givenSessionModel_whenProcessIsInvoked_thenSessionRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().getFirst().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(sessionsCaptor.getValue().getFirst().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(sessionsCaptor.getValue().getFirst().getPostHearingCustody())
                .isEqualTo(caseDetails.getSessions().getFirst().getPostHearingCustody());
    }

    @Test
    void givenSessionModelWithNullPostHearingCustody_whenProcessIsInvoked_thenSessionRecordIsCreated() {
        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.getSessions().getFirst().setPostHearingCustody(null);

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().getFirst().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(sessionsCaptor.getValue().getFirst().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(sessionsCaptor.getValue().getFirst().getPostHearingCustody()).isEqualTo(DEFAULT_HEARING_CUS_STATUS);
    }

    @Test
    void givenSessionModelWithNullHearingDate_whenProcessIsInvoked_thenSessionRecordIsNullFiltered() {
        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.getSessions().getFirst().setPostHearingCustody(null);

        courtDataDTO.getCaseDetails().getSessions().add(Session.builder().build());
        courtDataDTO
                .getCaseDetails()
                .getSessions()
                .add(Session.builder()
                        .courtLocation("B30PG")
                        .dateOfHearing(null)
                        .build());
        courtDataDTO
                .getCaseDetails()
                .getSessions()
                .add(Session.builder()
                        .courtLocation("B30PG")
                        .dateOfHearing("2021-04-30")
                        .build());

        // when
        sessionInfoProcessor.process(courtDataDTO);

        // then
        verify(sessionRepository).saveAll(sessionsCaptor.capture());
        assertThat(sessionsCaptor.getValue().getFirst().getCourtLocation()).isEqualTo("B16BG");
        assertThat(sessionsCaptor.getValue()).hasSize(2);
    }
}
