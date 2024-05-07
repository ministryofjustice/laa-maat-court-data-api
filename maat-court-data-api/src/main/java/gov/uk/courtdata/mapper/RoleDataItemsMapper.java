package gov.uk.courtdata.mapper;

import gov.uk.courtdata.dto.RoleDataItemDTO;
import gov.uk.courtdata.entity.RoleDataItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface RoleDataItemsMapper {

    List<RoleDataItemDTO> roleDataItemEntitytoDTO(final List<RoleDataItemEntity> roleDataItem);
}
