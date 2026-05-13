package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "REP_ORDER_MVO", schema = "TOGDATA")
public class RepOrderMvoEntity {
    @OneToMany(mappedBy = "mvo")
    private final Set<RepOrderMvoRegEntity> repOrderMvoRegEntities = new LinkedHashSet<>();

    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REP_ID", nullable = false)
    private RepOrderEntity rep;

    @Size(max = 1)
    @NotNull
    @Column(name = "VEHICLE_OWNER", nullable = false, length = 1)
    private String vehicleOwner;

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Size(max = 100)
    @NotNull
    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Size(max = 100)
    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;
}
