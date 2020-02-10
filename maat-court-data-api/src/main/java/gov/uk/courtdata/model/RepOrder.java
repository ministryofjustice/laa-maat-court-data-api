package gov.uk.courtdata.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepOrder {


    private Integer repOrderId;
    private String caseUrn;
    private String defendantId;
    private LocalDate dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
}
