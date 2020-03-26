package gov.uk.courtdata.model.laastatus;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
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
