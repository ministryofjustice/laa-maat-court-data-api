package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.model.contribution.CreateContributions;
import gov.uk.courtdata.model.contribution.UpdateContributions;
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
