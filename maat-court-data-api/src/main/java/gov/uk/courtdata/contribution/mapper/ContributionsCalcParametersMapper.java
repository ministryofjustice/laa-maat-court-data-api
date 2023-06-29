package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.entity.ContribCalcParametersEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface ContributionsCalcParametersMapper {
    ContributionCalcParametersDTO contributionsEntityToContributionsDTO(final ContribCalcParametersEntity entity);
}
