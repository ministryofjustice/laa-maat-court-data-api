package gov.uk.courtdata.wqhearing.mapper;

import gov.uk.courtdata.dto.WQHearingDTO;
import gov.uk.courtdata.entity.WQHearingEntity;

import java.util.List;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface WQHearingMapper {

    List<WQHearingDTO> wQHearingEntityToWQHearingDTO(List<WQHearingEntity> wqHearingEntityList);
}
