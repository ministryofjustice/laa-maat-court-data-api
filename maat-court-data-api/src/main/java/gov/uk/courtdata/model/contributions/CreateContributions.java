package gov.uk.courtdata.model.contributions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateContributions extends Contributions {
    private Integer repId;
    private Integer applId;
    private String userCreated;
}
