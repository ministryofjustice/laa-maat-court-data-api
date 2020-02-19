package gov.uk.courtdata.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unlink {

    private Integer maatId;
    private String userId;
    private Integer reasonId;
    private String reasonText;

}
