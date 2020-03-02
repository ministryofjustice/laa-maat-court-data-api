package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.SessionRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.DEFAULT_HEARING_CUS_STATUS;

@Component
@RequiredArgsConstructor
public class SessionInfoProcessor implements Process {

    private final CourtDataUtil courtDataUtil;
    private final SessionRepository sessionRepository;

    @Override
    public void process(LaaModelManager laaModelManager) {

        List<SessionEntity> sessionEntityList = laaModelManager.getCaseDetails().getSessions()
                .stream()
                .map(s -> buildSession(s, laaModelManager))
                .collect(Collectors.toList());
        sessionRepository.saveAll(sessionEntityList);
    }

    private SessionEntity buildSession(Session session, LaaModelManager saveAndLinkModel) {
        return SessionEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .dateOfHearing(LocalDate.parse(session.getDateOfHearing()))
                .courtLocation(session.getCourtLocation())
                .postHearingCustody(session.getPostHearingCustody() != null ? session.getPostHearingCustody() : DEFAULT_HEARING_CUS_STATUS)
                .sessionvalidatedate(courtDataUtil.getDate(session.getSessionvalidateddate()))
                .build();
    }
}
