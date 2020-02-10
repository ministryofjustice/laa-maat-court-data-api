package gov.uk.courtdata.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_PROCEEDING", schema = "MLA")
public class ProceedingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MAAT_ID")
    private Integer maatId;
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
