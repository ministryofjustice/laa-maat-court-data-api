package gov.uk.courtdata.users.mapper;

import gov.uk.courtdata.dto.FeatureToggleDTO;
import gov.uk.courtdata.dto.ReservationsDTO;
import gov.uk.courtdata.dto.RoleDataItemDTO;
import gov.uk.courtdata.dto.UserSummaryDTO;
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
    @Mapping(target = "reservationsDTO", source = "reservationsDTO")
    @Mapping(target = "roleDataItem", source = "roleDataItem")
    @Mapping(target = "featureToggle", source = "featureToggle")
    UserSummaryDTO userToUserSummaryDTO(final String username,
                                        final List<String> newWorkReasons,
                                        final List<String> roleActions,
                                        final ReservationsDTO reservationsDTO,
                                        final String currentUserSession,
                                        final List<RoleDataItemDTO> roleDataItem,
                                        final List<FeatureToggleDTO> featureToggle);
}
