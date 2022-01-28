package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardshipReviewService {

    private final HardshipReviewImpl hardshipReviewImpl;
    private final HardshipReviewMapper hardshipReviewMapper;

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public HardshipReviewDTO find(Integer hardshipReviewId) {
        HardshipReviewEntity hardshipReview = hardshipReviewImpl.find(hardshipReviewId);
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReview);
    }

    public HardshipReviewDTO create(CreateHardshipReview hardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.CreateHardshipReviewToHardshipReviewDTO(hardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.create(hardshipReviewDTO);
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }
}
