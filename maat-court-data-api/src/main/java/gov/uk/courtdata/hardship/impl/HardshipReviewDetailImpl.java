package gov.uk.courtdata.hardship.impl;

import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.repository.HardshipReviewDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HardshipReviewDetailImpl {

    private final HardshipReviewDetailRepository hardshipReviewDetailRepository;

    public List<HardshipReviewDetailEntity> find(Integer hardshipReviewId) {
        return hardshipReviewDetailRepository.findAllByHardshipReviewId(hardshipReviewId);
    }
}
