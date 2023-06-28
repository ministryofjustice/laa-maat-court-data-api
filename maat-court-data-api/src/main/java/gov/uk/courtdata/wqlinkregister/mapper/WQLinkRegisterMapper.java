package gov.uk.courtdata.wqlinkregister.mapper;

import gov.uk.courtdata.dto.WQLinkRegisterDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)public interface WQLinkRegisterMapper {

    List<WQLinkRegisterDTO> wQLinkRegisterToWQLinkRegisterDTO(List<WqLinkRegisterEntity> wqLinkRegisterEntities);
}
