package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hardship.impl.HardshipReviewDetailImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.impl.HardshipReviewProgressImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HardshipReviewService {

    private final HardshipReviewImpl hardshipReviewImpl;
    private final HardshipReviewMapper hardshipReviewMapper;
    private final HardshipReviewDetailImpl hardshipReviewDetailImpl;
    private final HardshipReviewProgressImpl hardshipReviewProgressImpl;

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public HardshipReviewDTO find(Integer hardshipReviewId) {
        HardshipReviewEntity hardshipReview = hardshipReviewImpl.find(hardshipReviewId);
        List<HardshipReviewDetailEntity> detailEntities = hardshipReviewDetailImpl.find(hardshipReviewId);
        List<HardshipReviewProgressEntity> progressEntities = hardshipReviewProgressImpl.find(hardshipReviewId);
        return buildHardshipReviewDTO(hardshipReview, detailEntities, progressEntities);
    }

    public HardshipReviewDTO buildHardshipReviewDTO(HardshipReviewEntity hardshipReview, List<HardshipReviewDetailEntity> detailEntities, List<HardshipReviewProgressEntity> reviewProgresses) {
        HardshipReviewDTO hardshipReviewDTO = hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReview);
        if (detailEntities != null) {
            hardshipReviewDTO.setReviewDetails(
                    detailEntities
                            .stream()
                            .map(hardshipReviewMapper::HardshipReviewDetailEntityToHardshipReviewDetail)
                            .collect(Collectors.toList())
            );
        }

        if (reviewProgresses != null) {
            hardshipReviewDTO.setReviewProgresses(
                    reviewProgresses
                            .stream()
                            .map(hardshipReviewMapper::HardshipReviewProgressEntityToHardshipReviewProgress)
                            .collect(Collectors.toList())
            );
        }

        return hardshipReviewDTO;
    }

    public HardshipReviewDTO buildHardshipReviewDTO(HardshipReviewEntity hardshipReview) {
        return buildHardshipReviewDTO(hardshipReview, null, null);
    }

}
