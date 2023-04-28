package gov.uk.courtdata.eform.mapper;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EformStagingDTOMapper {

    EformsStagingEntity toEformsStagingEntity(EformStagingDTO eformStagingDTO);

    EformStagingDTO toEformStagingDTO(EformsStagingEntity eformsStagingEntity);

    EformStagingResponse toEformStagingResponse(EformStagingDTO eformStagingDTO);

    EformsStagingEntity toEformsStagingEntity(EformsStagingEntity eformsStagingEntity);
}