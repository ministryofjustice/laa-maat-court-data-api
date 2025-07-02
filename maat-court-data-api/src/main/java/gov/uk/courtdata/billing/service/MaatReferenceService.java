package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.repository.MaatReferenceRepository;
import gov.uk.courtdata.exception.RecordsAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaatReferenceService {
    
    private final MaatReferenceRepository maatReferenceRepository;

    @Transactional
    public ResponseEntity<Void> populateTable() {
        if (!isTableEmpty()) {
            throw new RecordsAlreadyExistException("The MAAT_REFS_TO_EXTRACT table already has entries");
        }
        
        maatReferenceRepository.populateMaatReferences();
        return ResponseEntity.ok().build();
    }
    
    private boolean isTableEmpty() {
        return maatReferenceRepository.count() == 0;
    }
}
