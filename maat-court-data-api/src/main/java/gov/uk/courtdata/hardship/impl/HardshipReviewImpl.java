package gov.uk.courtdata.hardship.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.repository.HardshipReviewDetailReasonRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class HardshipReviewImpl {

    private final HardshipReviewMapper hardshipReviewMapper;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final HardshipReviewDetailReasonRepository hardshipReviewDetailReasonRepository;

    public HardshipReviewEntity find(Integer hardshipReviewId) {
        return hardshipReviewRepository.getReferenceById(hardshipReviewId);
    }

    public HardshipReviewEntity findByRepId(int repId) {
        return hardshipReviewRepository.findByRepId(repId);
    }

    public List<HardshipReviewEntity> findByDetailType(String detailType, int repId) {
        return hardshipReviewRepository.findByDetailType(detailType, repId);
    }

    public HardshipReviewEntity create(final HardshipReviewDTO hardshipReviewDTO) {
        HardshipReviewEntity hardshipReview = hardshipReviewMapper.hardshipReviewDTOToHardshipReviewEntity(hardshipReviewDTO);
        hardshipReview.getReviewDetails().forEach(
                detail -> detail.setDetailReason(
                        hardshipReviewDetailReasonRepository.getReferenceById(detail.getDetailReason().getId())
                )
        );
        return hardshipReviewRepository.saveAndFlush(hardshipReview);
    }

    public HardshipReviewEntity update(final HardshipReviewDTO hardshipReviewDTO) {

        HardshipReviewEntity existing = hardshipReviewRepository.getReferenceById(hardshipReviewDTO.getId());

        existing.setCmuId(hardshipReviewDTO.getCmuId());
        existing.setReviewResult(hardshipReviewDTO.getReviewResult());
        existing.setResultDate(hardshipReviewDTO.getResultDate());
        existing.setReviewDate(hardshipReviewDTO.getReviewDate());
        existing.setNotes(hardshipReviewDTO.getNotes());
        existing.setDecisionNotes(hardshipReviewDTO.getDecisionNotes());
        existing.setSolicitorRate(hardshipReviewDTO.getSolicitorCosts().getSolicitorRate());
        existing.setSolicitorHours(hardshipReviewDTO.getSolicitorCosts().getSolicitorHours());
        existing.setSolicitorVat(hardshipReviewDTO.getSolicitorCosts().getSolicitorVat());
        existing.setSolicitorDisb(hardshipReviewDTO.getSolicitorCosts().getSolicitorDisb());
        existing.setSolicitorEstTotalCost(hardshipReviewDTO.getSolicitorCosts().getSolicitorEstTotalCost());
        existing.setDisposableIncome(hardshipReviewDTO.getDisposableIncome());
        existing.setDisposableIncomeAfterHardship(hardshipReviewDTO.getDisposableIncomeAfterHardship());
        existing.setStatus(hardshipReviewDTO.getStatus());
        existing.setUserModified(hardshipReviewDTO.getUserModified());
        existing.setNewWorkReason(hardshipReviewMapper.newWorkReasonToNewWorkReasonEntity(hardshipReviewDTO.getNewWorkReason()));

        List<HardshipReviewDetail> detailItems = hardshipReviewDTO.getReviewDetails();
        existing.getReviewDetails().clear();
        if (!detailItems.isEmpty()) {
            detailItems.forEach(
                    detail -> {
                        HardshipReviewDetailEntity reviewDetailEntity = hardshipReviewMapper.hardshipReviewDetailToHardshipReviewDetailEntity(detail);
                        existing.addReviewDetail(reviewDetailEntity);
                    }
            );
        }

        List<HardshipReviewProgress> progressItems = hardshipReviewDTO.getReviewProgressItems();
        existing.getReviewProgressItems().clear();
        if (!progressItems.isEmpty()) {
            progressItems.forEach(
                    progress -> {
                        HardshipReviewProgressEntity reviewProgressEntity = hardshipReviewMapper.hardshipReviewProgressToHardshipReviewProgressEntity(progress);
                        existing.addReviewProgressItem(reviewProgressEntity);
                    }
            );
        }
        return hardshipReviewRepository.saveAndFlush(existing);
    }
}
