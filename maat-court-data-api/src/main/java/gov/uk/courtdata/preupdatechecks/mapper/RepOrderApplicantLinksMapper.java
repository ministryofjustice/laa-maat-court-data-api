package gov.uk.courtdata.preupdatechecks.mapper;

import gov.uk.courtdata.preupdatechecks.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.preupdatechecks.entity.RepOrderApplicantLinksEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface RepOrderApplicantLinksMapper {
    List<RepOrderApplicantLinksDTO> mapEntityToDTO(List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntity);

}
