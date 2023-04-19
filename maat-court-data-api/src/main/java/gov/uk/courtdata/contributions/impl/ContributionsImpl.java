package gov.uk.courtdata.contributions.impl;

import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.repository.ContributionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ContributionsImpl {

    private final ContributionsRepository contributionsRepository;
    public ContributionsEntity findLatest(Integer repId) {
        return contributionsRepository.findByRepIdAndLatestIsTrue(repId);
    }

    public ContributionsEntity update(ContributionsEntity contributionsEntity) {
        return contributionsRepository.saveAndFlush(contributionsEntity);
    }

    public ContributionsEntity create(ContributionsEntity contributionsEntity) {
        contributionsRepository.setEntryAsInactive(contributionsEntity.getRepId(), contributionsEntity.getEffectiveDate());
        contributionsRepository.setEntryAsPrior(contributionsEntity.getRepId());
        // Handle if the above queries don't run correctly???
        return contributionsRepository.saveAndFlush(contributionsEntity);
    }
}
