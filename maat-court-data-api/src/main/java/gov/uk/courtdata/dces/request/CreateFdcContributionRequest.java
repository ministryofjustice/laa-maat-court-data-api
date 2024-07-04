package gov.uk.courtdata.dces.request;

import gov.uk.courtdata.enums.FdcContributionsStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFdcContributionRequest {
    @Schema(description = "The REP ID when creating FdcContribution", example = "123")
    @NotNull
    private int repId;

    @Schema(description = "LGFS completion status", example = "Y", allowableValues = "Y,N")
    @Size(max = 1)
    private String lgfsComplete;

    @Size(max = 1)
    private String agfsComplete;

    @Size(max = 1)
    private String manualAcceleration;

    private FdcContributionsStatus status;
}
