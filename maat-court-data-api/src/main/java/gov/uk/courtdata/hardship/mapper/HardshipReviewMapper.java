package gov.uk.courtdata.hardship.mapper;

import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import gov.uk.courtdata.model.assessment.HardshipReviewDetail;
import gov.uk.courtdata.model.assessment.HardshipReviewProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HardshipReviewMapper {

    @Mapping(target = "solicitorCosts.solicitorRate", source = "solicitorRate")
    @Mapping(target = "solicitorCosts.solicitorHours", source = "solicitorHours")
    @Mapping(target = "solicitorCosts.solicitorVat", source = "solicitorVat")
    @Mapping(target = "solicitorCosts.solicitorDisb", source = "solicitorDisb")
    @Mapping(target = "solicitorCosts.solicitorEstTotalCost", source = "solicitorEstTotalCost")
    HardshipReviewDTO HardshipReviewEntityToHardshipReviewDTO(final HardshipReviewEntity hardshipReview);

    HardshipReviewDetail HardshipReviewDetailEntityToHardshipReviewDetail(final HardshipReviewDetailEntity reviewDetailEntity);

    HardshipReviewProgress HardshipReviewProgressEntityToHardshipReviewProgress(final HardshipReviewProgressEntity reviewProgressEntity);
}
