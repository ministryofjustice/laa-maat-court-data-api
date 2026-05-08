package gov.uk.courtdata.applicant.mapper;

import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;

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
public interface ApplicantHistoryMapper {
    ApplicantHistoryDTO mapEntityToDTO(ApplicantHistoryEntity applicantHistoryEntity);

    void updateApplicantHistoryDTOToApplicantHistoryEntity(
            ApplicantHistoryDTO applicantHistoryDTO, @MappingTarget ApplicantHistoryEntity applicantHistoryEntity);
}
