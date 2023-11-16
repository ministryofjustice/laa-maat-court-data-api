package gov.uk.courtdata.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderApplicantLinksDTO {
    @NotNull
    private Integer id;
    private Integer repId;
    private Integer partnerApplId;
    private LocalDate linkDate;
    private LocalDate unlinkDate;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private Integer partnerAphiId;
}
