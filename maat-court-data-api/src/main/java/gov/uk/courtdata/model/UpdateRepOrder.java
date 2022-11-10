package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRepOrder {

    private Integer repId;
    private LocalDateTime sentenceOrderDate;
    private String userModified;
}
