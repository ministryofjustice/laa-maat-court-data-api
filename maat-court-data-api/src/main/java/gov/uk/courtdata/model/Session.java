package gov.uk.courtdata.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {


    private String courtLocation;
    private LocalDate dateOfHearing;
    private String postHearingCustody;
    private LocalDate sessionvalidateddate;
}
