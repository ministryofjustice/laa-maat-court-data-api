package gov.uk.courtdata.applicant.mapper;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;

import java.util.List;

import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface RepOrderApplicantLinksMapper {
    List<RepOrderApplicantLinksDTO> mapEntityToDTO(List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities);

    RepOrderApplicantLinksDTO mapEntityToDTO(RepOrderApplicantLinksEntity repOrderApplicantLinksEntity);

    void updateRepOrderApplicantLinksDTOToRepOrderApplicantLinksEntity(
            RepOrderApplicantLinksDTO repOrderApplicantLinksDTO,
            @MappingTarget RepOrderApplicantLinksEntity repOrderApplicantLinksEntity);

    RepOrderApplicantLinksEntity mapDTOToEntity(RepOrderApplicantLinksDTO repOrderApplicantLinksDTO);
}
