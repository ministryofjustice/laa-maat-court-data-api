package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
public class CourtHouseCodesId implements Serializable {

    private String code;

    private LocalDateTime effectiveDateFrom;

}
