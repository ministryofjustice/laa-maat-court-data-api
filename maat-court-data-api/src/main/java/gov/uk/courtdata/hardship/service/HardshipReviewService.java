package gov.uk.courtdata.hardship.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class HardshipReviewService {

    private final HardshipReviewImpl hardshipReviewImpl;
    private final HardshipReviewMapper hardshipReviewMapper;

    @Transactional(readOnly = true)
    public HardshipReviewDTO findHardshipReview(final Integer hardshipReviewId) {
        HardshipReviewEntity hardshipReview = hardshipReviewImpl.find(hardshipReviewId);
        if (hardshipReview == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for ID: %s", hardshipReviewId));
        }
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReview);
    }

    @Transactional(readOnly = true)
    public HardshipReviewDTO findHardshipReviewByRepId(final int repId) {
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.findByRepId(repId);
        if (hardshipReviewEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for REP ID: %s", repId));
        }
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional
    public HardshipReviewDTO createHardshipReview(final CreateHardshipReview createHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.CreateHardshipReviewToHardshipReviewDTO(createHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.create(hardshipReviewDTO);
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional
    public HardshipReviewDTO updateHardshipReview(UpdateHardshipReview updateHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.UpdateHardshipReviewToHardshipReviewDTO(updateHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.update(hardshipReviewDTO);
        return hardshipReviewMapper.HardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }
}
