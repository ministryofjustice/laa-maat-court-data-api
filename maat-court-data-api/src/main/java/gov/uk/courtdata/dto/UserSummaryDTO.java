package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private String username;
    private List<String> newWorkReasons;
    private List<String> roleActions;
    private ReservationsDTO reservationsDTO;
    private String currentUserSession;
    private List<RoleDataItemDTO> roleDataItem;
    private List<FeatureToggleDTO> featureToggle;
}
