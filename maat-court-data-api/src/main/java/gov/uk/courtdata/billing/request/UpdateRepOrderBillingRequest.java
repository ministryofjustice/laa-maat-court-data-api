package gov.uk.courtdata.billing.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRepOrderBillingRequest {
    @NotNull
    private String userModified;
    @NotNull
    private List<Integer> repOrderIds;
}
