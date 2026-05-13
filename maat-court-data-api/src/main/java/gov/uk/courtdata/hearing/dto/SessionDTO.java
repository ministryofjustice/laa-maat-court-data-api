package gov.uk.courtdata.hearing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    private String courtLocation;
    private String dateOfHearing;
    private String postHearingCustody;
    private String sessionValidatedDate;
}
