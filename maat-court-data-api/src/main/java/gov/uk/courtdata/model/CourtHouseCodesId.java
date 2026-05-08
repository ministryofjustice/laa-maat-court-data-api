package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtHouseCodesId implements Serializable {

    private String code;

    private LocalDateTime effectiveDateFrom;
}
