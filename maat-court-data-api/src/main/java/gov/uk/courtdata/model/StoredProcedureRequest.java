package gov.uk.courtdata.model;

import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.dto.application.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StoredProcedureRequest {

    private String dbPackageName;
    private String procedureName;
    private ApplicationDTO application;
    private UserDTO user;
}
