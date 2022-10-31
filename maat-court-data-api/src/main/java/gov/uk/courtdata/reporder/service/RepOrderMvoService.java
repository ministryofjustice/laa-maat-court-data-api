package gov.uk.courtdata.reporder.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.RepOrderMvoDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.impl.RepOrderMvoImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMvoMapper;
import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderMvoService {

    private final RepOrderMvoImpl repOrderMvoImpl;

    private final RepOrderMvoMapper repOrderMvoMapper;

    @Transactional(readOnly = true)
    public RepOrderMvoDTO findRepOrderMvoByRepIdAndVehicleOwner(Integer repId, String vehicleOwner) {
        RepOrderMvoEntityInfo repOrderMvoEntityInfo = repOrderMvoImpl.findRepOrderMvoByRepIdAndVehicleOwner(repId, vehicleOwner);
        if (repOrderMvoEntityInfo == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order MVO found for ID: %s", repId));
        }
        return repOrderMvoMapper.repOrderMvoEntityInfoToRepOrderMvoDTO(repOrderMvoEntityInfo);
    }

}
