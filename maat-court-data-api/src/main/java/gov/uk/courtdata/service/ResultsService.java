package gov.uk.courtdata.service;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ResultRepository;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultsService {

    private final WQResultRepository wqResultRepository;
    private final ResultRepository resultRepository;
    private final XLATResultRepository xlatResultRepository;

    @Transactional(readOnly = true)
    public List<Integer> findWQResultCodesByCaseIdAndAsnSeq(Integer caseId, String asnSeq) {
        List<Integer> resultCodes = wqResultRepository.findResultCodeByCaseIdAndAsnSeq(caseId, asnSeq);
        if (resultCodes == null) {
            throw new RequestedObjectNotFoundException(String.format("No WQ Result Codes found for CaseId: %d and Asn Seq: %s", caseId, asnSeq));
        }
        return resultCodes;
    }

    @Transactional(readOnly = true)
    public List<Integer> findResultCodesByCaseIdAndAsnSeq(Integer caseId, String asnSeq) {
        List<Integer> resultCodes = resultRepository.findResultCodeByCaseIdAndAsnSeq(caseId, asnSeq);
        if (resultCodes == null) {
            throw new RequestedObjectNotFoundException(String.format("No Result Codes found for CaseId: %d and Asn Seq: %s", caseId, asnSeq));
        }
        return resultCodes;
    }

    @Transactional(readOnly = true)
    public List<Integer> findXLATResultCodesForCCImprisonment() {
        List<XLATResultEntity> resultCodes = xlatResultRepository.fetchResultCodesForCCImprisonment();
        if (resultCodes == null) {
            throw new RequestedObjectNotFoundException("No XLAT Result Codes found for CC Imprisonment");
        }
        return resultCodes
                .stream()
                .map(XLATResultEntity::getCjsResultCode)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Integer> findXLATResultCodesForCCBenchWarrant() {
        List<XLATResultEntity> resultCodes = xlatResultRepository.findByCjsResultCodeIn();
        if (resultCodes == null) {
            throw new RequestedObjectNotFoundException("No XLAT Result Codes found for CC Bench Warrant");
        }
        return resultCodes
                .stream()
                .map(XLATResultEntity::getCjsResultCode)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Integer> findXLATResultCodesByWQTypeAndSubTypeCode(Integer wqType, Integer subType) {
        List<Integer> resultCodes = xlatResultRepository.findResultsByWQType(wqType, subType);
        if (resultCodes == null) {
            throw new RequestedObjectNotFoundException(String.format("No XLAT Result Codes found for WQType: %d and SubType: %d", wqType, subType));
        }
        return resultCodes;
    }
}
