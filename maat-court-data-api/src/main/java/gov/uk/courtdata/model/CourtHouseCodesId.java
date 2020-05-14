package gov.uk.courtdata.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtHouseCodesId implements Serializable {

    private String code;

    private LocalDateTime effectiveDateFrom;

}
