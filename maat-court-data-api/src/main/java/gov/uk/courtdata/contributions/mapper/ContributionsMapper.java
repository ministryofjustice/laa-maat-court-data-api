package gov.uk.courtdata.contributions.mapper;

import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionsMapper {

    ContributionsDTO mapEntityToDTO(ContributionsEntity entity);

    void updateContributionsToContributionsEntity(UpdateContributions updatecontributions, @MappingTarget ContributionsEntity contributionsEntity);

    ContributionsEntity createContributionsToContributionsEntity(CreateContributions createContributions);
}
