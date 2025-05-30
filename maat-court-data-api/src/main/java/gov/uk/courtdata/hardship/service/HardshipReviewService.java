package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.hardship.impl.HardshipReviewImpl;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HardshipReviewService {

    private final HardshipReviewImpl hardshipReviewImpl;
    private final HardshipReviewMapper hardshipReviewMapper;
    private final HardshipReviewRepository hardshipReviewRepository;

    @Transactional(readOnly = true)
    public HardshipReviewDTO find(final Integer hardshipReviewId) {
        HardshipReviewEntity hardshipReview = hardshipReviewImpl.find(hardshipReviewId);
        if (hardshipReview == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for ID: %s", hardshipReviewId));
        }
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReview);
    }

    @Transactional(readOnly = true)
    public HardshipReviewDTO findByRepId(final int repId) {
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.findByRepId(repId);
        if (hardshipReviewEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for REP ID: %s", repId));
        }
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional(readOnly = true)
    public List<HardshipReviewDetail> findDetails(String detailType, int repId) {

        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.findByRepId(repId);
        if (hardshipReviewEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for REP ID: %s", repId));
        }

        return hardshipReviewEntity.getReviewDetails().stream()
                .filter(item -> item.getDetailType().equals(detailType))
                .map(hardshipReviewMapper::hardshipReviewDetailEntityToHardshipReviewDetail)
                .collect(Collectors.toList());
    }

    @Transactional
    public HardshipReviewDTO create(final CreateHardshipReview createHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.createHardshipReviewToHardshipReviewDTO(createHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.create(hardshipReviewDTO);
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional
    public HardshipReviewDTO update(UpdateHardshipReview updateHardshipReview) {
        HardshipReviewDTO hardshipReviewDTO =
                hardshipReviewMapper.updateHardshipReviewToHardshipReviewDTO(updateHardshipReview);
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewImpl.update(hardshipReviewDTO);
        return hardshipReviewMapper.hardshipReviewEntityToHardshipReviewDTO(hardshipReviewEntity);
    }

    @Transactional
    public void patch(int hardshipReviewId, Map<String, Object> updateFields) {
        HardshipReviewEntity hardshipReviewEntity = hardshipReviewRepository.findById(hardshipReviewId)
            .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("No Hardship Review found for ID: %s", hardshipReviewId)));

        ReflectionHelper.updateEntityFromMap(hardshipReviewEntity, updateFields);
        hardshipReviewRepository.save(hardshipReviewEntity);
    }
}
