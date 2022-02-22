package gov.uk.courtdata.hardship.mapper;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface HardshipReviewMapper {

    @Mapping(target = "solicitorCosts.solicitorRate", source = "solicitorRate")
    @Mapping(target = "solicitorCosts.solicitorHours", source = "solicitorHours")
    @Mapping(target = "solicitorCosts.solicitorVat", source = "solicitorVat")
    @Mapping(target = "solicitorCosts.solicitorDisb", source = "solicitorDisb")
    @Mapping(target = "solicitorCosts.solicitorEstTotalCost", source = "solicitorEstTotalCost")
    HardshipReviewDTO HardshipReviewEntityToHardshipReviewDTO(final HardshipReviewEntity hardshipReview);

    HardshipReviewDetail HardshipReviewDetailEntityToHardshipReviewDetail(final HardshipReviewDetailEntity reviewDetailEntity);

    HardshipReviewDetailEntity HardshipReviewDetailToHardshipReviewDetailEntity(final HardshipReviewDetail reviewDetail);

    HardshipReviewProgress HardshipReviewProgressEntityToHardshipReviewProgress(final HardshipReviewProgressEntity reviewProgressEntity);

    HardshipReviewProgressEntity HardshipReviewProgressToHardshipReviewProgressEntity(final HardshipReviewProgress reviewProgress);

    NewWorkReason NewWorkReasonEntityToNewWorkReason(final NewWorkReasonEntity newWorkReason);

    NewWorkReasonEntity NewWorkReasonToNewWorkReasonEntity(final NewWorkReason newWorkReason);

    HardshipReviewDetailReason HardshipReviewDetailReasonEntityToHardshipReviewDetailReason(final HardshipReviewDetailReasonEntity detailReasonEntity);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    HardshipReviewDTO CreateHardshipReviewToHardshipReviewDTO(final CreateHardshipReview hardshipReview);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    HardshipReviewDTO UpdateHardshipReviewToHardshipReviewDTO(final UpdateHardshipReview hardshipReview);

    @InheritInverseConfiguration
    HardshipReviewEntity HardshipReviewDTOToHardshipReviewEntity(final HardshipReviewDTO hardshipReviewDTO);
}
