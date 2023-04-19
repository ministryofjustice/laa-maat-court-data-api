package gov.uk.courtdata.contribution.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.dto.ContributionAppealDTO;
import gov.uk.courtdata.repository.ContribAppealRulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class ContributionAppealService {

    private final ContribAppealRulesRepository contribAppealRulesRepository;

    @Transactional(readOnly = true)
    public BigDecimal getContributionAmount(final ContributionAppealDTO contribAppealDTO) {
        log.info("Get contribution amount for ContributionAppealDTO " + contribAppealDTO);
        return contribAppealRulesRepository.findContributionAmount(contribAppealDTO.getCaseType(), contribAppealDTO.getAppealType(),
                contribAppealDTO.getOutcome(), contribAppealDTO.getAssessmentResult());
    }
}
