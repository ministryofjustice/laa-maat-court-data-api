package gov.uk.courtdata.hardship.impl;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.hardship.mapper.HardshipReviewMapper;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static gov.uk.courtdata.hardship.specification.HardshipSpecification.hasRepId;
import static gov.uk.courtdata.hardship.specification.HardshipSpecification.isCurrent;

@Slf4j
@Component
@RequiredArgsConstructor
public class HardshipReviewImpl {

    private final HardshipReviewMapper hardshipReviewMapper;
    private final HardshipReviewRepository hardshipReviewRepository;

    public HardshipReviewEntity find(Integer hardshipReviewId) {
        return hardshipReviewRepository.getReferenceById(hardshipReviewId);
    }

    public HardshipReviewEntity findByRepId(int repId) {
        return hardshipReviewRepository.findOne(hasRepId(repId).and(isCurrent())).orElse(null);
    }

    public HardshipReviewEntity create(final HardshipReviewDTO hardshipReviewDTO) {
        HardshipReviewEntity hardshipReview =
                hardshipReviewMapper.hardshipReviewDTOToHardshipReviewEntity(hardshipReviewDTO);

        hardshipReviewRepository.replaceOldHardshipReviews(hardshipReview.getRepId(), hardshipReview.getCourtType());

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
        existing.setDisposableIncome(hardshipReviewDTO.getDisposableIncome());
        existing.setDisposableIncomeAfterHardship(hardshipReviewDTO.getDisposableIncomeAfterHardship());
        existing.setStatus(hardshipReviewDTO.getStatus().getStatus());
        existing.setUserModified(hardshipReviewDTO.getUserModified());
        existing.setNewWorkReason(
                hardshipReviewMapper.newWorkReasonToNewWorkReasonEntity(hardshipReviewDTO.getNewWorkReason()));

        if (hardshipReviewDTO.getSolicitorCosts() == null) {
            existing.setSolicitorRate(null);
            existing.setSolicitorHours(null);
            existing.setSolicitorVat(null);
            existing.setSolicitorDisb(null);
            existing.setSolicitorEstTotalCost(null);
        } else {
            existing.setSolicitorRate(hardshipReviewDTO.getSolicitorCosts().getRate());
            existing.setSolicitorHours(hardshipReviewDTO.getSolicitorCosts().getHours());
            existing.setSolicitorVat(hardshipReviewDTO.getSolicitorCosts().getVat());
            existing.setSolicitorDisb(hardshipReviewDTO.getSolicitorCosts().getDisbursements());
            existing.setSolicitorEstTotalCost(hardshipReviewDTO.getSolicitorCosts().getEstimatedTotal());
        }

        List<HardshipReviewDetail> detailItems = hardshipReviewDTO.getReviewDetails();
        existing.getReviewDetails().clear();
        if (!detailItems.isEmpty()) {
            detailItems.forEach(
                    detail -> {
                        HardshipReviewDetailEntity reviewDetailEntity =
                                hardshipReviewMapper.hardshipReviewDetailToHardshipReviewDetailEntity(detail);
                        existing.addReviewDetail(reviewDetailEntity);
                    }
            );
        }

        List<HardshipReviewProgress> progressItems = hardshipReviewDTO.getReviewProgressItems();
        existing.getReviewProgressItems().clear();
        if (!progressItems.isEmpty()) {
            progressItems.forEach(
                    progress -> {
                        HardshipReviewProgressEntity reviewProgressEntity =
                                hardshipReviewMapper.hardshipReviewProgressToHardshipReviewProgressEntity(progress);
                        existing.addReviewProgressItem(reviewProgressEntity);
                    }
            );
        }
        return hardshipReviewRepository.saveAndFlush(existing);
    }
}
