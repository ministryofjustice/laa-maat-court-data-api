package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicantPaymentDetailsDTO extends GenericDTO {
    private Integer paymentDay;
    private String accountNumber;
    private String sortCode;
    private String accountName;

    @Builder.Default
    private PaymentMethodDTO paymentMethod = new PaymentMethodDTO();
}
