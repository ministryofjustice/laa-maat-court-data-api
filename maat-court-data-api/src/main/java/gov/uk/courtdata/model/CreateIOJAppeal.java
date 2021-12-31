package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateIOJAppeal {
    @NotNull
    private Integer repId;
    @NotNull
    private LocalDateTime appealSetupDate;
    @NotNull
    private String nworCode;
    @NotNull
    private String userCreated;
    @NotNull
    private Integer cmuId;
    @NotNull
    private String iapsStatus;
    private String appealSetupResult;
    private String iderCode;
    private LocalDateTime decisionDate;
    private String decisionResult;
    private String notes;
}
