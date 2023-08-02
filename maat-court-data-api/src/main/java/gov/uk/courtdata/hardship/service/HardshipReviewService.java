package gov.uk.courtdata.hardship.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReview);
    }

    @Transactional(readOnly = true)
    public HardshipReviewDTO findHardshipReviewByRepId(final int repId) {
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.findByRepId(repId);
        if (hardshipReviewEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for REP ID: %s", repId));
        }
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional(readOnly = true)
    public List<HardshipReviewDetail> findHardshipReviewByDetailType(String detailType, int repId) {

        List<HardshipReviewDetailEntity> hardshipReviewDetailEntityList = hardshipReviewImpl.findByDetailType(detailType, repId);
        if (hardshipReviewDetailEntityList == null || hardshipReviewDetailEntityList.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for Detail Type: %s and REP ID: %d", detailType, repId));
        }
        List<HardshipReviewDetail> hardshipReviewDetailList = new ArrayList<>();
        for (HardshipReviewDetailEntity hardshipReviewDetailEntity : hardshipReviewDetailEntityList) {
            hardshipReviewDetailList.add(hardshipReviewMapper.hardshipReviewDetailEntityToHardshipReviewDetail(hardshipReviewDetailEntity));
        }
        return hardshipReviewDetailList;
    }

    @Transactional
    public HardshipReviewDTO createHardshipReview(final CreateHardshipReview createHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.createHardshipReviewToHardshipReviewDTO(createHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.create(hardshipReviewDTO);
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional
    public HardshipReviewDTO updateHardshipReview(UpdateHardshipReview updateHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.updateHardshipReviewToHardshipReviewDTO(updateHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.update(hardshipReviewDTO);
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }
}
