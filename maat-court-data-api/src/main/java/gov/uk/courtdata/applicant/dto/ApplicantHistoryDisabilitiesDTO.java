package gov.uk.courtdata.applicant.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHistoryDisabilitiesDTO {
    private Integer id;
    private Integer aphiId;
    private String disaDisability;
    private String userCreated;
    private String userModified;
}
