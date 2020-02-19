package gov.uk.courtdata.model;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnlinkModel {

    private Unlink unlink;
    private Integer txId;
    private WqLinkRegisterEntity wqLinkRegisterEntity;

}
