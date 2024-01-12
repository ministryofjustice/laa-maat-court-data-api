package gov.uk.courtdata.reporder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IOJAssessorDetails {

    @NotNull
    private String name;
    @NotNull
    private String userName;
}
