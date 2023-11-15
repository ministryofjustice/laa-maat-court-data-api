package gov.uk.courtdata.reporder.impl;

import gov.uk.courtdata.repository.RepOrderCapitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepOrderCapitalImpl {

    private final RepOrderCapitalRepository capitalRepository;

    public Integer getCapitalAssetCount(Integer repId) {
        return capitalRepository.getCapitalAssetCount(repId);
    }
}
