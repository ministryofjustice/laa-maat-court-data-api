package gov.uk.courtdata.model.laastatus;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Solicitor {

    private String firmName;
    private String contactName;
    private String laaAccountNumber;
    private String addressLine1;
    private String addressLine2;
    private String postcode;
    private String phone;
    private String email;
}
