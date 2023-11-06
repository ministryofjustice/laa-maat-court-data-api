package gov.uk.courtdata.entity;


import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_UNLINK_REASON", schema = "MLA")
public class UnlinkEntity {

    @Id
    @Column(name = "TX_ID")
    private int txId;
    @Column(name = "CASE_ID")
    private int caseId;
    @Column(name = "REASON_ID")
    private int reasonId;
    @Column(name = "OTHER_REASON")
    private String otherReason;
}
