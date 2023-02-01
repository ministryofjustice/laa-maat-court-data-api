package gov.uk.courtdata.ccoutcome.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.ccoutcome.impl.CCOutComeImpl;
import gov.uk.courtdata.ccoutcome.mapper.CCOutComeMapper;
import gov.uk.courtdata.dto.RepOrderCCOutComeDTO;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class CCOutComeService {

    private final CCOutComeImpl ccOutComeImpl;
    private final CCOutComeMapper mapper;

    @Transactional
    public RepOrderCCOutComeDTO createCCOutCome(RepOrderCCOutCome repOrderCCOutCome) {
        log.info("Create repOrder CC OutCome  - Transaction Processing -Start");
        RepOrderCCOutComeEntity savedEntity = ccOutComeImpl.createCCOutCome(mapper.RepOrderCCOutComeToRepOrderCCOutComeEntity(repOrderCCOutCome));
        return mapper.RepOrderCCOutComeEntityToRepOrderCCOutComeDTO(savedEntity);
    }

    @Transactional
    public void updateCCOutcome(RepOrderCCOutCome repOrderCCOutCome) {
        log.info("Update repOrder CC OutCome  - Transaction Processing - Start");
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = ccOutComeImpl.find(repOrderCCOutCome.getId());
        mapper.RepOrderCCOutComeToRepOrderCCOutComeEntity(repOrderCCOutCome, repOrderCCOutComeEntity);
        ccOutComeImpl.updateCCOutcome(repOrderCCOutComeEntity);
        log.info("update repOrder CC OutCome  - Transaction Processing - End");
    }

    @Transactional(readOnly = true)
    public List<RepOrderCCOutComeDTO> findByRepId(Integer repId) {
        log.info("Find repOrder CC OutCome  - Transaction Processing - Start");
        return mapper.RepOrderCCOutComeEntityToRepOrderCCOutComeDTO(ccOutComeImpl.findByRepId(repId));
    }

}
