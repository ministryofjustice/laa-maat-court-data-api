package gov.uk.courtdata.hardship.mapper;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.HardshipReviewDetailReason;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.mapstruct.*;
import uk.gov.justice.laa.crime.enums.HardshipReviewStatus;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface HardshipReviewMapper {

    @Mapping(target = "solicitorCosts.rate", source = "solicitorRate")
    @Mapping(target = "solicitorCosts.hours", source = "solicitorHours")
    @Mapping(target = "solicitorCosts.vat", source = "solicitorVat")
    @Mapping(target = "solicitorCosts.disbursements", source = "solicitorDisb")
    @Mapping(target = "solicitorCosts.estimatedTotal", source = "solicitorEstTotalCost")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatusToHardshipReviewStatusEnum")
    HardshipReviewDTO hardshipReviewEntityToHardshipReviewDTO(final HardshipReviewEntity hardshipReview);

    @Mapping(source = "detailReason", target = "detailReason", qualifiedByName = "mapReasonToReviewDetailReasonEnum")
    HardshipReviewDetail hardshipReviewDetailEntityToHardshipReviewDetail(
            final HardshipReviewDetailEntity reviewDetailEntity
    );

    @Mapping(target = "detailReason.reason",
            expression = "java(hardshipReviewDetailReason.getReason())"
    )
    HardshipReviewDetailEntity hardshipReviewDetailToHardshipReviewDetailEntity(
            final HardshipReviewDetail reviewDetail);

    HardshipReviewProgress hardshipReviewProgressEntityToHardshipReviewProgress(
            final HardshipReviewProgressEntity reviewProgressEntity
    );

    HardshipReviewProgressEntity hardshipReviewProgressToHardshipReviewProgressEntity(
            final HardshipReviewProgress reviewProgress
    );

    NewWorkReason newWorkReasonEntityToNewWorkReason(final NewWorkReasonEntity newWorkReason);

    NewWorkReasonEntity newWorkReasonToNewWorkReasonEntity(final NewWorkReason newWorkReason);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    @Mapping(target = "valid", source = "valid", defaultValue = "Y")
    HardshipReviewDTO createHardshipReviewToHardshipReviewDTO(final CreateHardshipReview hardshipReview);

    @Mapping(target = "newWorkReason.code", source = "nworCode")
    @Mapping(target = "valid", source = "valid", defaultValue = "Y")
    HardshipReviewDTO updateHardshipReviewToHardshipReviewDTO(final UpdateHardshipReview hardshipReview);

    @InheritInverseConfiguration
    HardshipReviewEntity hardshipReviewDTOToHardshipReviewEntity(final HardshipReviewDTO hardshipReviewDTO);

    @Named("mapReasonToReviewDetailReasonEnum")
    default HardshipReviewDetailReason mapReasonToReviewDetailReasonEnum(
            HardshipReviewDetailReasonEntity detailReason) {
        if (detailReason != null) {
            return HardshipReviewDetailReason.getFrom(detailReason.getReason());
        } else {
            return null;
        }
    }

    @Named("mapStatusToHardshipReviewStatusEnum")
    default HardshipReviewStatus mapStatusToHardshipReviewStatusEnum(String status) {
        if (status != null) {
            return HardshipReviewStatus.getFrom(status);
        }
        return null;
    }
}
