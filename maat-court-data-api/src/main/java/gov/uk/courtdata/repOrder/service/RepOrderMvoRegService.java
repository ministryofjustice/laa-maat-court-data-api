package gov.uk.courtdata.repOrder.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repOrder.impl.RepOrderMvoRegImpl;
import gov.uk.courtdata.repOrder.mapper.RepOrderMvoRegMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderMvoRegService {

    private final RepOrderMvoRegImpl repOrderMvoRegImpl;
    private final RepOrderMvoRegMapper repOrderMvoRegMapper;

    @Transactional(readOnly = true)
    public RepOrderMvoRegDTO findRepOrderMvoRegByRepId(Integer repId) {
        RepOrderMvoRegEntity repOrderMvoRegEntity = repOrderMvoRegImpl.findRepOrderMvoRegByRepId(repId);
        if (repOrderMvoRegEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order MVO Reg found for ID: %s", repId));
        }
        return repOrderMvoRegMapper.repOrderMvoRegEntityToRepOrderMvoRegDTO(repOrderMvoRegEntity);
    }

}
