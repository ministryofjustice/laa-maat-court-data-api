package gov.uk.courtdata.model.hearing;

import lombok.*;

@ToString
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
    private String sessionValidateDate;

}
