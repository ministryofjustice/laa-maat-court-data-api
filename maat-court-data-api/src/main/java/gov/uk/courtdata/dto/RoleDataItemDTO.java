package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link gov.uk.courtdata.applicant.entity.RoleDataItem}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDataItemDTO {
    private String roleName;
    private String dataItem;
    private String enabled;
    private String insertAllowed;
    private String updateAllowed;
}