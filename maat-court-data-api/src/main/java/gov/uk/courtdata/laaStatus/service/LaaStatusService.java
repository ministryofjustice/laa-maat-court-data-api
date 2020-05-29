package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.repository.IdentifierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
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
