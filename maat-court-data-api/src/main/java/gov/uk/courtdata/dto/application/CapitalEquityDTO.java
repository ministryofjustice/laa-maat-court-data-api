/**
 *
 */
package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CapitalEquityDTO extends GenericDTO {
    public static final String RESIDENTIAL_STATUS_OWNER = "OWNER";
    public static final String RESIDENTIAL_STATUS_NO_FIXED_ABODE = "NOFIXABODE";

    private Long usn;
    private Boolean available;
    private Boolean noCapitalDeclared;
    private Boolean suffientVeriToCoverCase;
    private Boolean verifiedEquityToCoverCase;
    private Boolean suffientDeclToCoverCase;
    private Boolean declaredEquityToCoverCase;
    private BigDecimal totalCapital;
    private BigDecimal totalEquity;
    private BigDecimal totalCapitalAndEquity;

    @Builder.Default
    private Collection<EquityDTO> equityObjects = new ArrayList<>();
    @Builder.Default
    private Collection<CapitalPropertyDTO> capitalProperties = new ArrayList<>();
    @Builder.Default
    private Collection<CapitalOtherDTO> capitalOther = new ArrayList<>();
    @Builder.Default
    private CapitalEvidenceSummaryDTO capitalEvidenceSummary = new CapitalEvidenceSummaryDTO();
    @Builder.Default
    private MotorVehicleOwnerDTO motorVehicleOwnerDTO = new MotorVehicleOwnerDTO();
}
