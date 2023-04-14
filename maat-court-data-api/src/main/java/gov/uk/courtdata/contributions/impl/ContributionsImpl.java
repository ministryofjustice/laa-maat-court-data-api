package gov.uk.courtdata.contributions.impl;

import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.repository.ContributionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContributionsImpl {

    private final ContributionsRepository contributionsRepository;
    public ContributionsEntity findLatest(Integer repId) {
        return contributionsRepository.findByRepIdAndLatestIsTrue(repId);
    }
}
