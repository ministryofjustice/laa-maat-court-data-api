package gov.uk.courtdata.dto;

import gov.uk.courtdata.entity.ReservationsEntity;
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
    private ReservationsEntity reservationsEntity;
    private String currentUserSession;
}
