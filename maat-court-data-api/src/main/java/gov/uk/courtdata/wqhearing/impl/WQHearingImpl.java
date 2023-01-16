package gov.uk.courtdata.wqhearing.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class WQHearingImpl {

    private final WQHearingRepository wqHearingRepository;

    public List<WQHearingEntity> findByMaatIdAndHearingUUID(Integer maatId, String hearingUUID) {
        return wqHearingRepository.findByMaatIdAndHearingUUID(maatId, hearingUUID);
    }


}
