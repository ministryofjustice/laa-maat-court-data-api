package gov.uk.courtdata.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link gov.uk.courtdata.entity.RepOrderMvoEntity} entity
 */
@Data
public class RepOrderMvoDTO implements Serializable {
    private final Integer id;
    @NotNull
    private final RepOrderDTO rep;
    @Size(max = 1)
    @NotNull
    private final String vehicleOwner;
    @NotNull
    private final Instant dateCreated;
    @Size(max = 100)
    @NotNull
    private final String userCreated;
    private final Instant dateModified;
    @Size(max = 100)
    private final String userModified;
}