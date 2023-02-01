package gov.uk.courtdata.ccoutcome.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class CCOutComeImpl {

    private final CrownCourtProcessingRepository courtProcessingRepository;

    public RepOrderCCOutComeEntity createCCOutCome(RepOrderCCOutComeEntity repOrderCCOutComeEntity) {
        return courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
    }

    public RepOrderCCOutComeEntity find(Integer id) {
        return courtProcessingRepository.getReferenceById(id);
    }
    public void updateCCOutcome(RepOrderCCOutComeEntity repOrderCCOutComeEntity) {
         courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
    }

    public List<RepOrderCCOutComeEntity> findByRepId(Integer repId) {
        return courtProcessingRepository.findByRepId(repId);
    }
}
