package gov.uk.courtdata.contribution.mapper;

import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.projection.ContributionsSummaryView;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContributionsMapper {

    @Mapping(target = "repId", source = "repOrder.id")
    ContributionsDTO mapEntityToDTO(ContributionsEntity entity);

    List<ContributionsDTO> mapEntityToDTO(List<ContributionsEntity> entity);

    @Mapping(target = "repOrder.id", source = "repId")
    ContributionsEntity createContributionsToContributionsEntity(CreateContributionRequest createContributions);

    List<ContributionsSummaryDTO> contributionsSummaryToContributionsSummaryDTO(List<ContributionsSummaryView> contributionsSummaryViews);
}
