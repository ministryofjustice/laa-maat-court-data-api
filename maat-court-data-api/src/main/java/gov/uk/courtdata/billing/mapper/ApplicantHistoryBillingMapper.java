package gov.uk.courtdata.billing.mapper;


import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true)
)
public interface ApplicantHistoryBillingMapper {
    ApplicantHistoryBillingDTO mapEntityToDTO(ApplicantHistoryBillingEntity applicantHistoryEntity);
}
