package gov.uk.courtdata.model.iojAppeal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class IOJAppeal {
    @NotNull
    private Integer repId;

    @NotNull
    private LocalDateTime appealSetupDate;

    @NotNull
    private String nworCode;

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
