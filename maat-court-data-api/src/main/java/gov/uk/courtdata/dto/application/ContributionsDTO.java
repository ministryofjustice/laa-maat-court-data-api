package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContributionsDTO extends GenericDTO {
    private Long id;
    private BigDecimal monthlyContribs;
    private BigDecimal upfrontContribs;
    private SysGenDate effectiveDate;
    private SysGenDate calcDate;
    private BigDecimal capped;
    private boolean upliftApplied;
    private SysGenString basedOn;
}
