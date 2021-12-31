package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOJAppealImpl {

    private final IOJAppealRepository iojAppealRepository;

    public IOJAppealEntity find(Integer iojAppealId) {
        return iojAppealRepository.getById(iojAppealId);
    }
}
