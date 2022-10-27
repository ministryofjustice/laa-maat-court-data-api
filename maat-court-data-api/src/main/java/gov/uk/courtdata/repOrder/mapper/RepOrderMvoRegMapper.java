package gov.uk.courtdata.repOrder.mapper;

import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface RepOrderMvoRegMapper {
    RepOrderMvoRegEntity repOrderMvoRegDTOToRepOrderMvoRegEntity(RepOrderMvoRegDTO repOrderMvoRegDTO);

    RepOrderMvoRegDTO repOrderMvoRegEntityToRepOrderMvoRegDTO(RepOrderMvoRegEntity repOrderMvoRegEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RepOrderMvoRegEntity updateRepOrderMvoRegEntityFromRepOrderMvoRegDTO(RepOrderMvoRegDTO repOrderMvoRegDTO, @MappingTarget RepOrderMvoRegEntity repOrderMvoRegEntity);
}
