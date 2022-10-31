package gov.uk.courtdata.reporder.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.impl.RepOrderMvoRegImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMvoRegMapper;
import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderMvoRegService {

    private final RepOrderMvoRegImpl repOrderMvoRegImpl;
    private final RepOrderMvoRegMapper repOrderMvoRegMapper;

    @Transactional(readOnly = true)
    public List<RepOrderMvoRegDTO> findByDateDeletedIsNull(Integer mvoId) {
        List<RepOrderMvoRegEntityInfo> repOrderMvoRegEntityInfo = repOrderMvoRegImpl.findByDateDeletedIsNull(mvoId);
        if (repOrderMvoRegEntityInfo == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order MVO Reg found for ID: %s", mvoId));
        }
        return repOrderMvoRegMapper.repOrderMvoRegEntityInfoToRepOrderMvoRegDTO(repOrderMvoRegEntityInfo);
    }

}
