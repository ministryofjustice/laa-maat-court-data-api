package gov.uk.courtdata.reporder.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class CCOutcomeService {

    private final CCOutcomeImpl ccOutComeImpl;
    private final CCOutcomeMapper mapper;

    @Transactional
    public RepOrderCCOutcomeDTO create(RepOrderCCOutcome repOrderCCOutCome) {
        log.info("Create repOrder CC OutCome  - Transaction Processing -Start");
        RepOrderCCOutComeEntity savedEntity = ccOutComeImpl.create(mapper.repOrderCCOutcomeToRepOrderCCOutcomeEntity(repOrderCCOutCome));
        return mapper.repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(savedEntity);
    }

    @Transactional
    public RepOrderCCOutcomeDTO update(RepOrderCCOutcome repOrderCCOutCome) {
        log.info("Update repOrder CC OutCome  - Transaction Processing - Start");
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = ccOutComeImpl.find(repOrderCCOutCome.getId());
        if (repOrderCCOutComeEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No CC Outcome found for ID: %s", repOrderCCOutCome.getId()));
        }
        mapper.repOrderCCOutComeMappedToRepOrderCCOutcomeEntity(repOrderCCOutCome, repOrderCCOutComeEntity);
        log.info("update repOrder CC OutCome  - Transaction Processing - End");
        return mapper.repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(ccOutComeImpl.update(repOrderCCOutComeEntity));
    }

    @Transactional(readOnly = true)
    public List<RepOrderCCOutcomeDTO> findByRepId(Integer repId) {
        log.info("Find repOrder CC OutCome  - Transaction Processing - Start");
        return mapper.repOrderCCOutComeEntityToRepOrderCCOutcomeDTO(ccOutComeImpl.findByRepId(repId));
    }

    @Transactional
    public Integer deleteByRepId(Integer repId) {
        log.info("Delete repOrder CC OutCome for repId {}", repId);
        Integer deletedCount = ccOutComeImpl.deleteByRepId(repId);
        log.info("{} rows were deleted for CCOutcome against repId {}", deletedCount, repId);

        return deletedCount;
    }

}
