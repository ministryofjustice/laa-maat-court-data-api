package gov.uk.courtdata.laastatus.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laastatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@XRayEnabled
@RequiredArgsConstructor
public class LaaStatusService {


    private final LaaStatusUpdateImpl laaStatusUpdateImpl;

    private final IdentifierRepository identifierRepository;


    public void execute(CourtDataDTO courtDataDTO) {

        mapTxnID(courtDataDTO);
        laaStatusUpdateImpl.execute(courtDataDTO);
    }

    private void mapTxnID(CourtDataDTO courtDataDTO) {

        courtDataDTO.setTxId(identifierRepository.getTxnID());

    }
}
