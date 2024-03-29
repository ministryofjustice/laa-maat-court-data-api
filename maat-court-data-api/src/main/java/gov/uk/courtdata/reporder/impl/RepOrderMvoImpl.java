package gov.uk.courtdata.reporder.impl;

import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;
import gov.uk.courtdata.repository.RepOrderMvoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RepOrderMvoImpl {

    private final RepOrderMvoRepository repOrderMvoRepository;

    public RepOrderMvoEntityInfo findRepOrderMvoByRepIdAndVehicleOwner(Integer id, String vehicleOwner) {
        return repOrderMvoRepository.findByRepIdAndVehicleOwner(id, vehicleOwner, RepOrderMvoEntityInfo.class);
    }

}
