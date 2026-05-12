package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Builder
@AllArgsConstructor
public class Session {

    private String courtLocation;
    private String dateOfHearing;
    private String postHearingCustody;
    private String sessionValidateDate;
}
