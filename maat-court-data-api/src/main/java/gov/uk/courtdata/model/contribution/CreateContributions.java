package gov.uk.courtdata.model.contribution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateContributions extends Contributions {
    private Integer repId;

    @NotNull(message = "Null applId value was provided")
    private Integer applId;

    @NotEmpty(message = "Null or blank userCreated value was provided")
    private String userCreated;
}
