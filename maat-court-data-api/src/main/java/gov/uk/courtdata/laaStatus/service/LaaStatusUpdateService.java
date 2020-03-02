package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laaStatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaaStatusUpdateService {


    private final DefendantMAATDataRepository defendantMAATDataRepository;
    private final SolicitorMAATDataRepository solicitorMAATDataRepository;
    private final LaaStatusUpdateImpl laaStatusUpdateImpl;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void execute(CaseDetails caseDetails) {
        LaaModelManager laaModelManager = buildModelManager(caseDetails);
        laaStatusUpdateImpl.execute(laaModelManager);
    }

    private LaaModelManager buildModelManager(CaseDetails caseDetails) {
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository.findBymaatId(caseDetails.getMaatId());
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterEntityList.get(0);
        return LaaModelManager.builder()
                .caseDetails(caseDetails)
                .caseId(wqLinkRegisterEntity.getCaseId())
                .libraId(Integer.parseInt(wqLinkRegisterEntity.getLibraId()))
                .proceedingId(wqLinkRegisterEntity.getProceedingId())
                .defendantMAATDataEntity(defendantMAATDataRepository.findBymaatId(caseDetails.getMaatId()).get())
                .solicitorMAATDataEntity(solicitorMAATDataRepository.findBymaatId(caseDetails.getMaatId()).get())
                .build();

    }
}
