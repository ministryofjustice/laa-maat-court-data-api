package gov.uk.courtdata.eformsApplication.mapper;

import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.entity.EformsStagingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EformsStagingDTOMapper {
    EformsStagingEntity toEformsStagingEntity(final EformsStagingDTO eformsStagingDTO);
}