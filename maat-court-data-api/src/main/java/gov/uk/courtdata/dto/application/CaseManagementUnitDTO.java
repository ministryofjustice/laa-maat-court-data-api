package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CaseManagementUnitDTO extends GenericDTO {
    private Long cmuId;
    private Long areaId;
    private Boolean enabled;
    private String code;
    private Boolean libraAccess;
    private String name;
    private String description;
    private Date timeStamp;
}
