package gov.uk.courtdata.billing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBillingRequest {
    @NotBlank(message = "userModified must not be blank")
    private String userModified;
    @NotEmpty(message = "Ids must not be empty")
    private List<Integer> ids;
}
