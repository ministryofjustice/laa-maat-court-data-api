package gov.uk.courtdata.entity;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_PROCEEDING", schema = "MLA")
public class ProceedingEntity {
    @Id
    @Column(name = "CREATED_TXID")
    private Integer createdTxid;
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "PROCEEDING_ID")
    private Integer proceedingId;
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "REMOVED_TXID")
    private Integer removedTxid;
    @Column(name = "REMOVED_USER")
    private String removedUser;
}
