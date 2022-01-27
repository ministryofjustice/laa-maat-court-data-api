package gov.uk.courtdata.hardship.impl;

import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.repository.HardshipReviewProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HardshipReviewProgressImpl {

    private final HardshipReviewProgressRepository hardshipReviewProgressRepository;

    public List<HardshipReviewProgressEntity> find(Integer hardshipReviewId) {
        return hardshipReviewProgressRepository.findByHardshipReviewId(hardshipReviewId);
    }
}
