package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private RepOrderMvoEntity mvo;

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
