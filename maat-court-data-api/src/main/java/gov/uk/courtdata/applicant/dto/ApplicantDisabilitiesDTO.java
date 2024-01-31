package gov.uk.courtdata.applicant.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
