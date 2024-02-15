/**
 *
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DrcProcessDTO extends GenericDTO {
    private long id;
    private String stage;
    private String subStage;
    private String status;
    private String stageTime;
    private BigDecimal collected;
    private String charges;
    private String type;
    private Date lastPaymentTime;


}
