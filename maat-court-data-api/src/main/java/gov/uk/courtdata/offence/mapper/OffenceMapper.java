package gov.uk.courtdata.offence.mapper;

import gov.uk.courtdata.dto.OffenceDTO;
import gov.uk.courtdata.entity.OffenceEntity;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface OffenceMapper {

    List<OffenceDTO> offenceEntityToOffenceDTO(final List<OffenceEntity> offenceEntity);
}
