package gov.uk.courtdata.dces.response;

import gov.uk.courtdata.enums.FdcContributionsStatus;
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
    private Integer maatId;
    private LocalDate sentenceOrderDate;
    private LocalDate dateCalculated;
    private BigDecimal finalCost;
    private BigDecimal lgfsCost;
    private BigDecimal agfsCost;
    private String userModified;
    private String userCreated;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private String accelerate;
    private BigDecimal judApportionPercent;
    private BigDecimal agfsVat;
    private Integer contFileId;
    private LocalDate dateReplaced;
    private FdcContributionsStatus status;
    private String lgfsComplete;
    private String agfsComplete;
    private BigDecimal vat;
    private BigDecimal lgfsVat;

}