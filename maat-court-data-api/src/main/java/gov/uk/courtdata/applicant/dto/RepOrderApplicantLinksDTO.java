package gov.uk.courtdata.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderApplicantLinksDTO {
    private Integer id;
    private Integer repId;
    private Integer partnerApplId;
    private LocalDate linkDate;
    private LocalDate unlinkDate;
    private String userCreated;
    private String userModified;
    private Integer partnerAphiId;
}
