package gov.uk.courtdata.iojAppeal.mapper;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.model.CreateIOJAppeal;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IOJAppealMapper {
    IOJAppealDTO toIOJAppealDTO(IOJAppealEntity iojAppealEntity);
    IOJAppealDTO toIOJAppealDTO(CreateIOJAppeal iojAppeal);
}
