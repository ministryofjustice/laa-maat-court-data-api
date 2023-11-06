package gov.uk.courtdata.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime dateCreated;
    @Size(max = 100)
    @NotNull
    private String userCreated;
    private LocalDate dateDeleted;
    private LocalDateTime dateModified;
    @Size(max = 100)
    private String userModified;
}