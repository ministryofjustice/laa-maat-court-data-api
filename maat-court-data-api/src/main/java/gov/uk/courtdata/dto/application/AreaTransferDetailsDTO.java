/**
 *
 */
package gov.uk.courtdata.dto.application;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AreaTransferDetailsDTO extends GenericDTO {

    private Long id;
    private Date dateHmcsSent;
    private Date dateHmcsReceived;
    private UserDTO hmcsSentBy;
    private UserDTO hmcsReceivedBy;

    @Builder.Default
    private AreaDTO areaFrom = new AreaDTO();
    @Builder.Default
    private AreaDTO areaTo = new AreaDTO();
    @Builder.Default
    private CaseManagementUnitDTO cmuFrom = new CaseManagementUnitDTO();
    @Builder.Default
    private CaseManagementUnitDTO cmuTo = new CaseManagementUnitDTO();
    @Builder.Default
    private TransferStatusDTO transferStatus = new TransferStatusDTO();
    @Builder.Default
    private TransferTypeDTO transferType = new TransferTypeDTO();
}
