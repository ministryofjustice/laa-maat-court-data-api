package gov.uk.courtdata.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendToCCLFDTO {
    @NotNull
    private Integer repId;

    @NotNull
    private Integer applId;

    @NotNull
    private Integer applHistoryId;
}
