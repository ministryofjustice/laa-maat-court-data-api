package gov.uk.courtdata.contributions.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.repository.ContributionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class ContributionsImpl {

    private final ContributionsRepository contributionsRepository;

    public ContributionsEntity find(int id) {
        log.debug("Retrieving contributions entry for ID {}", id);
        return contributionsRepository.findById(id).orElse(null);
    }

    public ContributionsEntity findLatest(Integer repId) {
        log.debug("Retrieving latest contributions entry for rep ID {}", repId);
        return contributionsRepository.findByRepIdAndLatestIsTrue(repId);
    }

    public ContributionsEntity update(ContributionsEntity contributionsEntity) {
        return contributionsRepository.saveAndFlush(contributionsEntity);
    }

    public Optional<Void> updateExistingContributions(Integer repId, LocalDate effectiveDate) {
        log.debug("Setting existing contributions row as prior and inactive");
        contributionsRepository.updateExistingContributionToInactive(repId, effectiveDate);
        contributionsRepository.updateExistingContributionToPrior(repId);
        return Optional.empty();
    }

    public ContributionsEntity create(ContributionsEntity contributionsEntity) {
        log.debug("Inserting new contributions row and setting as latest");
        contributionsEntity.setLatest(true);
        return contributionsRepository.saveAndFlush(contributionsEntity);
    }
}
