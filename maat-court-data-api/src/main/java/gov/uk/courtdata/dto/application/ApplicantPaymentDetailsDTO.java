package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApplicantPaymentDetailsDTO extends GenericDTO {

    private PaymentMethodDTO paymentMethod;
    private Integer paymentDay;
    private String accountNumber;
    private String sortCode;
    private String accountName;

}
