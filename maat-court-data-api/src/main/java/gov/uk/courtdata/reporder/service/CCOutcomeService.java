package gov.uk.courtdata.reporder.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.RepOrderCCOutcomeDTO;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.reporder.impl.CCOutcomeImpl;
import gov.uk.courtdata.reporder.mapper.CCOutcomeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class CCOutcomeService {

    private final CCOutcomeImpl ccOutComeImpl;
    private final CCOutcomeMapper mapper;

    @Transactional
    public RepOrderCCOutcomeDTO create(RepOrderCCOutcome repOrderCCOutCome) {
        log.info("Create repOrder CC OutCome  - Transaction Processing -Start");
        RepOrderCCOutComeEntity savedEntity = ccOutComeImpl.create(mapper.RepOrderCCOutcomeToRepOrderCCOutcomeEntity(repOrderCCOutCome));
        return mapper.RepOrderCCOutComeEntityToRepOrderCCOutcomeDTO(savedEntity);
    }

    @Transactional
    public void update(RepOrderCCOutcome repOrderCCOutCome) {
        log.info("Update repOrder CC OutCome  - Transaction Processing - Start");
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = ccOutComeImpl.find(repOrderCCOutCome.getId());
        if (repOrderCCOutComeEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No CC Outcome found for ID: %s", repOrderCCOutCome.getId()));
        }
        mapper.RepOrderCCOutComeToRepOrderCCOutcomeEntity(repOrderCCOutCome, repOrderCCOutComeEntity);
        ccOutComeImpl.update(repOrderCCOutComeEntity);
        log.info("update repOrder CC OutCome  - Transaction Processing - End");
    }

    @Transactional(readOnly = true)
    public List<RepOrderCCOutcomeDTO> findByRepId(Integer repId) {
        log.info("Find repOrder CC OutCome  - Transaction Processing - Start");
        return mapper.RepOrderCCOutComeEntityToRepOrderCCOutcomeDTO(ccOutComeImpl.findByRepId(repId));
    }

}
