package gov.uk.courtdata.applicant.dto;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryDisabilitiesEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDisabilitiesDTO {
    private Integer id;
    private Integer applId;
    private String disaDisability;
    private String userCreated;
    private String userModified;
    @Builder.Default
    private final List<ApplicantHistoryDisabilitiesEntity> applicantHistoryDisabilityEntities = new ArrayList<>();
}
