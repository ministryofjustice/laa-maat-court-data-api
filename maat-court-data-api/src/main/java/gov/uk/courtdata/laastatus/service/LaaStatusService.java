package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laastatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.repository.CaseRepository;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LaaStatusService {

    private final LaaStatusUpdateImpl laaStatusUpdateImpl;

    private final IdentifierRepository identifierRepository;

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    private final OffenceRepository offenceRepository;

    private final CaseRepository caseRepository;

    public void execute(CourtDataDTO courtDataDTO) {

        mapTxnID(courtDataDTO);
        laaStatusUpdateImpl.execute(courtDataDTO);
    }

    private void mapTxnID(CourtDataDTO courtDataDTO) {

        courtDataDTO.setTxId(identifierRepository.getTxnID());
    }

    public Optional<WqLinkRegisterEntity> getUnLinkDetails(Integer repId) {
        return wqLinkRegisterRepository.findUnlinkByMaat(repId);
    }

    public Optional<OffenceEntity> getOffenceDetails(Integer caseId, String offenceCode) {
        return offenceRepository.getOffenceDetail(caseId, offenceCode);
    }

    public List<WqLinkRegisterEntity> getLinkingDetails(Integer repId) {
        return wqLinkRegisterRepository.findBymaatId(repId);
    }

    public Optional<CaseEntity> getCaseDetails(Integer caseId, Integer txId) {
        return caseRepository.getCaseDetail(caseId, txId);
    }

    @Transactional
    public OffenceEntity saveOffence(OffenceEntity wq) {
        return offenceRepository.save(wq);
    }

    @Transactional
    public WqLinkRegisterEntity saveLink(WqLinkRegisterEntity wq) {
        return wqLinkRegisterRepository.save(wq);
    }

    @Transactional
    public CaseEntity saveCase(CaseEntity wq) {
        return caseRepository.save(wq);
    }
}
