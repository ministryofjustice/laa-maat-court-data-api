package gov.uk.courtdata.mapper;

import gov.uk.courtdata.dto.RoleDataItemDTO;
import gov.uk.courtdata.entity.RoleDataItemEntity;

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
public interface RoleDataItemsMapper {

    List<RoleDataItemDTO> roleDataItemEntitytoDTO(final List<RoleDataItemEntity> roleDataItem);
}
