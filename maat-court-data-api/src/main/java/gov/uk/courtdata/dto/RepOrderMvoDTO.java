package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link gov.uk.courtdata.entity.RepOrderMvoEntity} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderMvoDTO implements Serializable {
    private Integer id;
    @NotNull
    private RepOrderDTO rep;
    @Size(max = 1)
    @NotNull
    private String vehicleOwner;
    @NotNull
    private LocalDateTime dateCreated;
    @Size(max = 100)
    @NotNull
    private String userCreated;
    private LocalDateTime dateModified;
    @Size(max = 100)
    private String userModified;
}