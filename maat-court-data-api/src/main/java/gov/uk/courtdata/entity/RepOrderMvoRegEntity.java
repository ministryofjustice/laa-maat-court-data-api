package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "REP_ORDER_MVO_REG", schema = "TOGDATA")
public class RepOrderMvoRegEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MVO_ID", nullable = false)
    private gov.uk.courtdata.entity.RepOrderMvoEntity mvo;

    @Size(max = 10)
    @Column(name = "REGISTRATION", length = 10)
    private String registration;

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Size(max = 100)
    @NotNull
    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_DELETED")
    private LocalDateTime dateDeleted;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Size(max = 100)
    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

}