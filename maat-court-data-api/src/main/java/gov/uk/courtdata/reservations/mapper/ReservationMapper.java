package gov.uk.courtdata.reservations.mapper;

import gov.uk.courtdata.dto.ReservationDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReservationMapper {

    ReservationsEntity reservationDTOToReservationEntity(final ReservationDTO reservation);

    ReservationDTO reservationEntityToReservationDTO(final ReservationsEntity reservation);
}