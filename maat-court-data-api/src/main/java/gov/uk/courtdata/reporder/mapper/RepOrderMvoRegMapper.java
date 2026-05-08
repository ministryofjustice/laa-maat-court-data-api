package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.reporder.projection.RepOrderMvoRegEntityInfo;

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
public interface RepOrderMvoRegMapper {
    List<RepOrderMvoRegDTO> repOrderMvoRegEntityInfoToRepOrderMvoRegDTO(
            List<RepOrderMvoRegEntityInfo> repOrderMvoRegEntityInfo);
}
