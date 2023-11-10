package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DrcFileRefDTO extends GenericDTO {

    private Long contribFileId;
    private Date dateSent;
    private Date dateAcknowledged;
    private String acknowledgeCode;

}
