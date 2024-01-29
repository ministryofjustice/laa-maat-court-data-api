package gov.uk.courtdata.dces.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdcContributionEntry {
    private Integer id;
    private LocalDate sentenceOrderDate;
    private LocalDate dateCalculated;
    private BigDecimal finalCost;
    private BigDecimal lgfsCost;
    private BigDecimal agfsCost;
}