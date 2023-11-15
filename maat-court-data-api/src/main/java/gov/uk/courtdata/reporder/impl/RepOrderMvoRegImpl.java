package gov.uk.courtdata.reporder.impl;

import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;
import gov.uk.courtdata.repository.RepOrderMvoRegRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepOrderMvoRegImpl {

    private final RepOrderMvoRegRepository repOrderMvoRegRepository;

    public List<RepOrderMvoRegEntityInfo> findByCurrentMvoRegistration(Integer mvoId) {
        return repOrderMvoRegRepository.findByMvoIdAndAndDateDeletedIsNull(mvoId);
    }

}
