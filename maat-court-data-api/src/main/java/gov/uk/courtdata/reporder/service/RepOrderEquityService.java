package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.repository.RepOrderEquityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepOrderEquityService {

    private final RepOrderEquityRepository repOrderEquityRepository;

    @Transactional(readOnly = true)
    public RepOrderEquityEntity retrieve(Integer id) {
        return repOrderEquityRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("No Rep Order Equity found with ID: %s", id)));
    }

    @Transactional()
    public void create(RepOrderEquityEntity repOrderEquityEntity) {
        repOrderEquityRepository.save(repOrderEquityEntity);
    }

    @Transactional()
    public void update(Integer id, RepOrderEquityEntity repOrderEquityEntity) {
        Optional<RepOrderEquityEntity> existingRepOrderEquity = repOrderEquityRepository.findById(id);

        if (existingRepOrderEquity.isPresent()) {
            for (Field declaredField: RepOrderEquityEntity.class.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(declaredField);
                Object fieldValue = ReflectionUtils.getField(declaredField, repOrderEquityEntity);
                if (fieldValue != null) {
                    ReflectionUtils.setField(declaredField, existingRepOrderEquity.get(), fieldValue);
                }
            }

            repOrderEquityRepository.save(existingRepOrderEquity.get());
        } else {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order Equity found with ID: %s", id));
        }
    }

    @Transactional
    public void delete(Integer id) {
        repOrderEquityRepository.deleteById(id);
    }
}
