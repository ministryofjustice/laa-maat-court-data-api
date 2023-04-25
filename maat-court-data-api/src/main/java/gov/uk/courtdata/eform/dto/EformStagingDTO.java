package gov.uk.courtdata.eform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformStagingDTO implements Serializable {
    
    private Integer usn;
    private String type;
}
