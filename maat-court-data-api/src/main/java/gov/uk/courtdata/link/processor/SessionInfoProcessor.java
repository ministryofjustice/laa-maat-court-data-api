package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.DEFAULT_HEARING_CUS_STATUS;
import static gov.uk.courtdata.util.DateUtil.parse;

@Component
@RequiredArgsConstructor
public class SessionInfoProcessor implements Process {

    private final SessionRepository sessionRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        List<SessionEntity> sessionEntityList = courtDataDTO.getCaseDetails().getSessions()
                .stream()
                .map(s -> buildSession(s, courtDataDTO))
                .collect(Collectors.toList());
        sessionRepository.saveAll(sessionEntityList);
    }

    private SessionEntity buildSession(Session session, CourtDataDTO saveAndLinkModel) {
        return SessionEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .dateOfHearing(LocalDate.parse(session.getDateOfHearing()))
                .courtLocation(session.getCourtLocation())
                .postHearingCustody(session.getPostHearingCustody() != null ? session.getPostHearingCustody() : DEFAULT_HEARING_CUS_STATUS)
                .sessionvalidatedate(parse(session.getSessionvalidateddate()))
                .build();
    }
}
