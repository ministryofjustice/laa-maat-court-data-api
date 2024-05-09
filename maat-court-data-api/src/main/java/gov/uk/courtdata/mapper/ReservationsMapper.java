package gov.uk.courtdata.mapper;

import gov.uk.courtdata.dto.ReservationsDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface ReservationsMapper {

    ReservationsDTO reservationsEntitytoDTO(final ReservationsEntity roleDataItem);
}
