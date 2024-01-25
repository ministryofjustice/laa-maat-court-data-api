package gov.uk.courtdata.applicant.mapper;

import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryDisabilitiesEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface ApplicantHistoryDisabilitiesMapper {
    ApplicantDisabilitiesDTO mapEntityToDTO(ApplicantDisabilitiesEntity applicantDisabilitiesEntity);

    @Mapping(target = "applicantDisabilities.id", source = "aphiId")
    ApplicantHistoryDisabilitiesEntity applicantHistoryDisabilitiesDTOToApplicantHistoryDisabilitiesEntity(final ApplicantHistoryDisabilitiesDTO applicantHistoryDisabilitiesDTO);
}
