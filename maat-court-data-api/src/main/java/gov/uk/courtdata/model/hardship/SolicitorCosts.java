package gov.uk.courtdata.model.hardship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitorCosts {
    private Double solicitorRate;
    private Double solicitorHours;
    private Double solicitorVat;
    private Double solicitorDisb;
    private Double solicitorEstTotalCost;
}
