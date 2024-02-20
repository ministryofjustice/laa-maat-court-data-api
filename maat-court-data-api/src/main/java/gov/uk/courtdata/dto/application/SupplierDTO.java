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
public class SupplierDTO extends GenericDTO {
    private String name;
    private String accountNumber;

    @Builder.Default
    private AddressDTO address = new AddressDTO();
}