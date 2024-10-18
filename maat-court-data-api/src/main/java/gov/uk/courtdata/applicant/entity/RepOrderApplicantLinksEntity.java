package gov.uk.courtdata.applicant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "REP_ORDER_APPLICANT_LINKS", schema = "TOGDATA")
public class RepOrderApplicantLinksEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "rep_order_applicant_links_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rep_order_applicant_links_gen_seq")
    private Integer id;

    @Column(name = "REP_ID")
    private Integer repId;

    @Column(name = "PARTNER_APPL_ID")
    private Integer partnerApplId;

    @Column(name = "LINK_DATE")
    private LocalDate linkDate;

    @Column(name = "UNLINK_DATE")
    private LocalDate unlinkDate;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "PARTNER_APHI_ID")
    private Integer partnerAphiId;

}
