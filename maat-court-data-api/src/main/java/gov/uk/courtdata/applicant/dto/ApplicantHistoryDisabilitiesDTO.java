package gov.uk.courtdata.applicant.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryDisabilities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHistoryDisabilitiesDTO {
    private Integer id;
    private String disaDisability;
    private String userCreated;
    private String userModified;
}
