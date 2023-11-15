package gov.uk.courtdata.entity;


import gov.uk.courtdata.model.id.ProceedingMaatId;
import lombok.*;

import jakarta.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ProceedingMaatId.class)
@Table(name = "XXMLA_PROCEEDING", schema = "MLA")
public class ProceedingEntity {

    @Id
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Id
    @Column(name = "PROCEEDING_ID")
    private Integer proceedingId;
    @Column(name = "CREATED_TXID")
    private Integer createdTxid;
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "REMOVED_TXID")
    private Integer removedTxid;
    @Column(name = "REMOVED_USER")
    private String removedUser;
}
