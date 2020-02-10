package gov.uk.courtdata.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proceeding {

    private Integer maatId;
    private Integer proceedingId;
    private Integer createdTxid;
    private String createdUser;
    private Integer removedTxid;
    private String removedUser;
}
