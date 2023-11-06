package gov.uk.courtdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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