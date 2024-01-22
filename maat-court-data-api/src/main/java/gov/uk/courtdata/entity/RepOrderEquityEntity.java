package gov.uk.courtdata.entity;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "REP_ORDER_EQUITY", schema = "TOGDATA")
public class RepOrderEquityEntity {

    @Id
    @Column(name = "ID", nullable = false)
    @NotNull
    @SequenceGenerator(name = "rep_order_equity_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rep_order_equity_seq")
    private Integer id;

    @NotNull
    @Column(name = "REP_ID")
    private Integer repId;

    @Nullable
    @Column(name = "ROPD_ID")
    private Integer repOrderPropertyDetailsId;

    @Size(max = 20)
    @Nullable
    @Column(name = "ASST_ASSET_STATUS", length = 20)
    private String asstAssetStatus;

    @Size(max = 50)
    @Nullable
    @Column(name = "VERIFIED_BY")
    private String verifiedBy;

    @Nullable
    @Column(name = "VERIFIED_DATE")
    private LocalDate verifiedDate;

    @Nullable
    @Column(name = "DATE_ENTERED")
    private LocalDate dateEntered;

    @Size(max = 1)
    @Nullable
    @Column(name = "UNDECLARED", length = 1)
    private String undeclared;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Size(max = 100)
    @NotNull
    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Nullable
    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Size(max = 100)
    @Nullable
    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Size(max = 1)
    @Nullable
    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Nullable
    @Column(name = "REMOVED_DATE")
    private LocalDate removedDate;
}