package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laaStatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaaStatusUpdateService {


    private final LaaStatusUpdateImpl laaStatusUpdateImpl;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final SolicitorMAATDataRepository solicitorMAATDataRepository;
    private final DefendantMAATDataRepository defendantMAATDataRepository;

    public void execute(CaseDetails caseDetails) {
        CourtDataDTO courtDataDTO = buildCourtData(caseDetails);
        laaStatusUpdateImpl.execute(courtDataDTO);
    }

    private CourtDataDTO buildCourtData(CaseDetails caseDetails) {
        final Integer maatId = caseDetails.getMaatId();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository.findBymaatId(maatId);
        SolicitorMAATDataEntity solicitorMAATDataEntity = solicitorMAATDataRepository.findBymaatId(maatId).get();
        DefendantMAATDataEntity defendantMAATDataEntity = defendantMAATDataRepository.findBymaatId(maatId).get();
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterEntityList.get(0);
        return CourtDataDTO.builder()
                .caseDetails(caseDetails)
                .caseId(wqLinkRegisterEntity.getCaseId())
                .libraId(Integer.parseInt(wqLinkRegisterEntity.getLibraId()))
                .proceedingId(wqLinkRegisterEntity.getProceedingId())
                .defendantMAATDataEntity(defendantMAATDataEntity)
                .solicitorMAATDataEntity(solicitorMAATDataEntity)
                .build();

    }
}
