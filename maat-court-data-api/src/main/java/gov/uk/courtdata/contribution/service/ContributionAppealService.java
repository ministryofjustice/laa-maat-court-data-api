package gov.uk.courtdata.contribution.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.repository.ContribAppealRulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class ContributionAppealService {

    private final ContribAppealRulesRepository contribAppealRulesRepository;

    @Transactional
    public Integer getContributionAmount(final String caseType, final String appealType, final String outcome, final String result) {
        return contribAppealRulesRepository.findContributionAmount(caseType, appealType, outcome, result);
    }
}
