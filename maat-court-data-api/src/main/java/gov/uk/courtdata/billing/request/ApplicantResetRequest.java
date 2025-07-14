package gov.uk.courtdata.billing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ApplicantResetRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotEmpty(message = "Applicant IDs must not be empty")
    private List<Integer> applicantIds;
}