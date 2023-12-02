package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserRoleDTO extends GenericDTO {

    public static final String NCT_CASEWORKER = "NCT CASEWORKER";

    private String roleName;
    private boolean enabled;

}
