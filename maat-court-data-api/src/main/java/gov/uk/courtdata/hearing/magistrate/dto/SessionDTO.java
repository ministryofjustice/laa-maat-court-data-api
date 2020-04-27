package gov.uk.courtdata.hearing.magistrate.dto;


import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    private String courtLocation;
    private String dateOfHearing;
    private String postHearingCustody;
    private String sessionvalidateddate;

}
