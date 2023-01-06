package gov.uk.courtdata.offence.mapper;

import gov.uk.courtdata.dto.OffenceDTO;
import gov.uk.courtdata.entity.OffenceEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface OffenceMapper {

    List<OffenceDTO> offenceEntityToOffenceDTO(final List<OffenceEntity> offenceEntity);
}
