package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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