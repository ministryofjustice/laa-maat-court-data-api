package gov.uk.courtdata.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link gov.uk.courtdata.entity.RepOrderMvoRegEntity} entity
 */
@Data
public class RepOrderMvoRegDTO implements Serializable {
    private final Integer id;
    @NotNull
    private final RepOrderMvoDTO mvo;
    @Size(max = 10)
    private final String registration;
    @NotNull
    private final Instant dateCreated;
    @Size(max = 100)
    @NotNull
    private final String userCreated;
    private final LocalDate dateDeleted;
    private final Instant dateModified;
    @Size(max = 100)
    private final String userModified;
}