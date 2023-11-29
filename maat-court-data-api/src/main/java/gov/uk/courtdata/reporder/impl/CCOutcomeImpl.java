package gov.uk.courtdata.reporder.impl;

import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CCOutcomeImpl {

    private final CrownCourtProcessingRepository courtProcessingRepository;

    public RepOrderCCOutComeEntity create(RepOrderCCOutComeEntity repOrderCCOutComeEntity) {
        return courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
    }

    public RepOrderCCOutComeEntity find(Integer id) {
        return courtProcessingRepository.findById(id).orElse(null);
    }
    public RepOrderCCOutComeEntity update(RepOrderCCOutComeEntity repOrderCCOutComeEntity) {
         return courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
    }

    public List<RepOrderCCOutComeEntity> findByRepId(Integer repId) {
        return courtProcessingRepository.findByRepOrder_Id(repId);
    }
}
