package gov.uk.courtdata.applicant.mapper;

import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface ApplicantDisabilitiesMapper {
    ApplicantDisabilitiesDTO mapEntityToDTO(ApplicantDisabilitiesEntity applicantDisabilitiesEntity);
    void updateApplicantDisabilitiesDTOToApplicantDisabilitiesEntity(ApplicantDisabilitiesDTO applicantDisabilitiesDTO, @MappingTarget ApplicantDisabilitiesEntity applicantDisabilitiesEntity);

}
