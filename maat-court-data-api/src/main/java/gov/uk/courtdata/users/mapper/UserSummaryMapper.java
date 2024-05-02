package gov.uk.courtdata.users.mapper;

import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface UserSummaryMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "reservationsEntity", source = "reservationsEntity")
    UserSummaryDTO userToUserSummaryDTO(final String username,
                                        final List<String> newWorkReasons,
                                        final List<String> roleActions,
                                        final ReservationsEntity reservationsEntity,
                                        final String currentUserSession
                                        );
}
