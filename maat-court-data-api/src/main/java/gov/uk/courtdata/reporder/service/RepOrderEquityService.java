package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.reporder.repository.RepOrderEquityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepOrderEquityService {

    private final RepOrderEquityRepository repOrderEquityRepository;

    @Transactional(readOnly = true)
    public RepOrderEquityEntity retrieve(Integer repOrderEquityId) {
        return repOrderEquityRepository.findById(repOrderEquityId)
            .orElseThrow(() -> new RequestedObjectNotFoundException(
                String.format("No Rep Order Equity found with ID: %s", repOrderEquityId)));
    }

    @Transactional()
    public void create(RepOrderEquityEntity repOrderEquityEntity) {
        repOrderEquityRepository.save(repOrderEquityEntity);
    }

    @Transactional()
    public void update(Integer id, RepOrderEquityEntity repOrderEquityEntity) {
        RepOrderEquityEntity existingRepOrderEquity = repOrderEquityRepository.findById(id)
            .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("No Rep Order Equity found with ID: %s", id)));

        ReflectionHelper.updateEntityFromObject(existingRepOrderEquity, repOrderEquityEntity);
        repOrderEquityRepository.save(existingRepOrderEquity);
    }

    @Transactional
    public void delete(Integer repOrderEquityId) {
        repOrderEquityRepository.deleteById(repOrderEquityId);
    }
}
