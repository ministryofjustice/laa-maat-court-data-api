package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FrequenciesDTO extends GenericDTO {

    private Long assCritId;
    private String code;
    private String description;
    private Long annualWeighting;

}
