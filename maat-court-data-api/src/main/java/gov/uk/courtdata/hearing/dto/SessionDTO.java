package gov.uk.courtdata.hearing.dto;


import lombok.*;


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
