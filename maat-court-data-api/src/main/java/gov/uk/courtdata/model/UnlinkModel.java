package gov.uk.courtdata.model;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UnlinkModel {

    private Unlink unlink;
    private Integer txId;
    private WqLinkRegisterEntity wqLinkRegisterEntity;
    private RepOrderCPDataEntity repOrderCPDataEntity;
}
