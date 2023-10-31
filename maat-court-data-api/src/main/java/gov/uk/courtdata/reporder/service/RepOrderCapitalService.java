package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.reporder.impl.RepOrderCapitalImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepOrderCapitalService {

    private final RepOrderCapitalImpl capitalImpl;

    @Transactional
    public Integer getCapitalAssetCount(Integer repId) {
        log.info("Get capital Asset count  - Transaction Processing -Start");
        return capitalImpl.getCapitalAssetCount(repId);
    }
}
