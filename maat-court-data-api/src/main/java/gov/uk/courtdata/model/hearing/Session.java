package gov.uk.courtdata.model.hearing;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private String courtLocation;
    private String dateOfHearing;
    private String postHearingCustody;
    private String sessionValidateDate;



}
