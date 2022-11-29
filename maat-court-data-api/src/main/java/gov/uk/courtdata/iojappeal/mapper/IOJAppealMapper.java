package gov.uk.courtdata.iojappeal.mapper;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IOJAppealMapper {
    IOJAppealDTO toIOJAppealDTO(IOJAppealEntity iojAppealEntity);

    @Mapping(target = "replaced", constant = "N")
    IOJAppealDTO toIOJAppealDTO(CreateIOJAppeal iojAppeal);

    IOJAppealDTO toIOJAppealDTO(UpdateIOJAppeal iojAppeal);

    IOJAppealEntity toIOJIojAppealEntity(IOJAppealDTO iojAppealDTO);
}
