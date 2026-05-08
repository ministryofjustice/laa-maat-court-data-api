package gov.uk.courtdata.applicant.mapper;

import gov.uk.courtdata.applicant.dto.ApplicantDTO;
import gov.uk.courtdata.entity.Applicant;

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
public interface ApplicantMapper {
    ApplicantDTO mapEntityToDTO(Applicant applicant);

    Applicant mapDTOToEntity(ApplicantDTO applicantDTO);

    Applicant updateApplicantEntity(ApplicantDTO applicantDTO, @MappingTarget Applicant applicant);
}
