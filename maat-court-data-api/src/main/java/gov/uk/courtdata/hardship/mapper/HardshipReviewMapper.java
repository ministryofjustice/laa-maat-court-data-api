package gov.uk.courtdata.hardship.mapper;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.*;
import org.mapstruct.*;

import java.util.List;

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
    HardshipReviewDTO hardshipReviewEntityToHardshipReviewDTO(final HardshipReviewEntity hardshipReview);

    HardshipReviewDetail hardshipReviewDetailEntityToHardshipReviewDetail(final HardshipReviewDetailEntity reviewDetailEntity);

    HardshipReviewDetailEntity hardshipReviewDetailToHardshipReviewDetailEntity(final HardshipReviewDetail reviewDetail);

    HardshipReviewProgress hardshipReviewProgressEntityToHardshipReviewProgress(final HardshipReviewProgressEntity reviewProgressEntity);

    HardshipReviewProgressEntity hardshipReviewProgressToHardshipReviewProgressEntity(final HardshipReviewProgress reviewProgress);

    NewWorkReason newWorkReasonEntityToNewWorkReason(final NewWorkReasonEntity newWorkReason);

    NewWorkReasonEntity newWorkReasonToNewWorkReasonEntity(final NewWorkReason newWorkReason);

    HardshipReviewDetailReason hardshipReviewDetailReasonEntityToHardshipReviewDetailReason(final HardshipReviewDetailReasonEntity detailReasonEntity);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    HardshipReviewDTO createHardshipReviewToHardshipReviewDTO(final CreateHardshipReview hardshipReview);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    HardshipReviewDTO updateHardshipReviewToHardshipReviewDTO(final UpdateHardshipReview hardshipReview);

    @InheritInverseConfiguration
    HardshipReviewEntity hardshipReviewDTOToHardshipReviewEntity(final HardshipReviewDTO hardshipReviewDTO);
}
