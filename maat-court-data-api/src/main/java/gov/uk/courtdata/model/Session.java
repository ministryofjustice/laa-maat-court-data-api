package gov.uk.courtdata.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {


    private String courtLocation;
    private String dateOfHearing;
    private String postHearingCustody;
    private String sessionvalidateddate;


}
