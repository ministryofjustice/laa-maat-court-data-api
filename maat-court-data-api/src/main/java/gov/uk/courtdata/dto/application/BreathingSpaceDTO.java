package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BreathingSpaceDTO extends GenericDTO {

    private Long id;
    private String active;  //yes or no
    private String type; //mental health or standard
    private Long repOrderId;
    private Date startDate;
    private Date endDate;
    private Long debtAmount;
    private String debtRefNumber;

    private Date dateCreated;
    private String userCreated;
    private Timestamp dateModified;
    private String userModified;

}