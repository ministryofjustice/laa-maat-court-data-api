package gov.uk.courtdata.model.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigParametersId implements Serializable {
    private String code;
    private LocalDateTime effectiveDate;
}
