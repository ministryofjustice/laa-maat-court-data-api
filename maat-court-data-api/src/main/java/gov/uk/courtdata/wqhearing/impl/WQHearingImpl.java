package gov.uk.courtdata.wqhearing.impl;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WQHearingImpl {

    private final WQHearingRepository wqHearingRepository;

    public List<WQHearingEntity> findByMaatIdAndHearingUUID(Integer maatId, String hearingUUID) {
        return wqHearingRepository.findByMaatIdAndHearingUUID(maatId, hearingUUID);
    }
}
