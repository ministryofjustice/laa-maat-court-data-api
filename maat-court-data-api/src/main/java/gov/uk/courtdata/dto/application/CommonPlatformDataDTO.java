package gov.uk.courtdata.dto.application;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CommonPlatformDataDTO extends GenericDTO {

    private Long repOrderId;
    private String caseURN;
    private String defendantId;
    private Timestamp dateCreated;
    private String userCreated;
    private Timestamp dateModified;
    private String userModified;
    private String inCommonPlatform;

}