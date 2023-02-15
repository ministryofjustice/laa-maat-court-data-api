package gov.uk.courtdata.eformsApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Clob;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformsStagingDTO implements Serializable {
    private Integer usn;
    private String type;
    private String xmlDoc;
    private String maatRef;
    private String maatStatus;
    private String userCreated;
}
