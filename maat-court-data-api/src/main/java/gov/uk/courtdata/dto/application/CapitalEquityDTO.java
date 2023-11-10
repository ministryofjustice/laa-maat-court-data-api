/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CapitalEquityDTO extends GenericDTO
{
    public static final String RESIDENTIAL_STATUS_OWNER = "OWNER";
    public static final String RESIDENTIAL_STATUS_NO_FIXED_ABODE = "NOFIXABODE";

    private Boolean							available;
	private Boolean                         noCapitalDeclared;
    private Boolean                         suffientVeriToCoverCase;
    private Boolean                         verifiedEquityToCoverCase;
    private Boolean                         suffientDeclToCoverCase;
    private Boolean                         declaredEquityToCoverCase;
    private SysGenCurrency                  totalCapital;
    private Currency                        totalEquity;
    private Currency                        totalCapitalAndEquity;
    private Collection<EquityDTO>           equityObjects;
    private Collection<CapitalPropertyDTO>  capitalProperties;
    private Collection<CapitalOtherDTO>     capitalOther;
    private CapitalEvidenceSummaryDTO       capitalEvidenceSummary;
  
    private MotorVehicleOwnerDTO motorVehicleOwnerDTO;
    
    private Long 							usn;
}
