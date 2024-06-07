package gov.uk.courtdata.dces.response;

import gov.uk.courtdata.enums.ConcorContributionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionResponseDTO {

    @NotBlank
    private Integer id;
    private Integer repId;
    private LocalDate dateCreated;
    private String userCreated;
    private LocalDate dateModified;
    private String userModified;
    private Integer seHistoryId;
    @NotBlank
    private ConcorContributionStatus status;
    private Integer contribFileId;
    private Integer ackFileId;
    private String ackCode;
}