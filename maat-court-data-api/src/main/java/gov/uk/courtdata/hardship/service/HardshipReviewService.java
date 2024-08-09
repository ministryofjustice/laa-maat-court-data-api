package gov.uk.courtdata.hardship.service;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import uk.gov.justice.laa.crime.enums.HardshipReviewDetailType;
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
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                .filter(item -> item.getDetailType() == HardshipReviewDetailType.getFrom(detailType))
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
        Optional<HardshipReviewEntity> hardshipReviewEntityOptional = hardshipReviewRepository.findById(hardshipReviewId);
        if (hardshipReviewEntityOptional.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("No Hardship Review found for ID: %s", hardshipReviewId));
        } else {
            HardshipReviewEntity hardshipReviewEntity = hardshipReviewEntityOptional.get();
            updateFields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(HardshipReviewEntity.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, hardshipReviewEntity, value);
            });
            hardshipReviewRepository.save(hardshipReviewEntity);
        }
    }
}
