package gov.uk.courtdata.hardship.mapper;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
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

    HardshipReviewDetail hardshipReviewDetailEntityToHardshipReviewDetail(
            final HardshipReviewDetailEntity reviewDetailEntity
    );

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

    @Named("mapStatusToHardshipReviewStatusEnum")
    default HardshipReviewStatus mapStatusToHardshipReviewStatusEnum(String status) {
        if (status != null) {
            return HardshipReviewStatus.getFrom(status);
        }
        return null;
    }
}
