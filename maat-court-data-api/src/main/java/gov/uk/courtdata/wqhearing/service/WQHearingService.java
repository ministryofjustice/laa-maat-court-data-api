package gov.uk.courtdata.wqhearing.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.WQHearingDTO;
import gov.uk.courtdata.wqhearing.impl.WQHearingImpl;
import gov.uk.courtdata.wqhearing.mapper.WQHearingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class WQHearingService {

    private final WQHearingImpl wqHearingImpl;

    private final WQHearingMapper mapper;

    @Transactional(readOnly = true)
    public List<WQHearingDTO> findByMaatIdAndHearingUUID(Integer maatId, String hearingUUID) {
        log.info("WQ Hearing service - Find by maat id  - Start");
        return mapper.wQHearingEntityToWQHearingDTO(wqHearingImpl.findByMaatIdAndHearingUUID(maatId, hearingUUID));
    }
}
