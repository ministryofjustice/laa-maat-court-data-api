package gov.uk.courtdata.correspondence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrespondenceStateDTO {

    @NotNull
    private Integer repId;

    @NotBlank
    private String status;

}
