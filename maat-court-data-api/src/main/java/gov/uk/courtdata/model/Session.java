package gov.uk.courtdata.model;

import lombok.*;

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
