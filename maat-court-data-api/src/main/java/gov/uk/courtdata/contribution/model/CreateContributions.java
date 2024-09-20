package gov.uk.courtdata.contribution.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateContributions extends Contributions {
    private Integer repId;

    @NotNull(message = "Null applicantId value was provided")
    private Integer applicantId;

    @NotEmpty(message = "Null or blank userCreated value was provided")
    private String userCreated;
}
