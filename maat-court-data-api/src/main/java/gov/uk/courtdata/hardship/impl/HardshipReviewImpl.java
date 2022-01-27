package gov.uk.courtdata.hardship.impl;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HardshipReviewImpl {

    private final HardshipReviewRepository hardshipReviewRepository;

    public HardshipReviewEntity find(Integer hardshipReviewId) {
        return hardshipReviewRepository.getById(hardshipReviewId);
    }
}
