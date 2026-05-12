package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderMvoDTO;
import gov.uk.courtdata.entity.RepOrderMvoEntity;
import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;

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
public interface RepOrderMvoMapper {
    RepOrderMvoDTO repOrderMvoEntityInfoToRepOrderMvoDTO(RepOrderMvoEntityInfo repOrderMvoEntity);

    List<RepOrderMvoDTO> repOrderMvoEntityToRepOrderMvoDTO(List<RepOrderMvoEntity> repOrderMvoEntity);
}
