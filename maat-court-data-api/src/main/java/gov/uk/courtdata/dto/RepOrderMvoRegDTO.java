package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link gov.uk.courtdata.entity.RepOrderMvoRegEntity} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderMvoRegDTO implements Serializable {
    private Integer id;
    @NotNull
    private RepOrderMvoDTO mvo;
    @Size(max = 10)
    private String registration;
    @NotNull
    private Instant dateCreated;
    @Size(max = 100)
    @NotNull
    private String userCreated;
    private LocalDate dateDeleted;
    private Instant dateModified;
    @Size(max = 100)
    private String userModified;
}