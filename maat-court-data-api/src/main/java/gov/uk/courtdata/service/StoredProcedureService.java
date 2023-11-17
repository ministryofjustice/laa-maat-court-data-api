package gov.uk.courtdata.service;

import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.repository.StoredProcedureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoredProcedureService {

    private final StoredProcedureRepository repository;

    @Transactional
    public ApplicationDTO executeStoredProcedure(StoredProcedureRequest callStoredProcedure) {
        log.info("Calling execute Store Procedure- Start");
        return repository.executeStoredProcedure(callStoredProcedure);
    }

}
