package gov.uk.courtdata.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "ROLE_DATA_ITEMS", schema = "TOGDATA")
public class RoleDataItemEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "DATA_ITEM")
    private String dataItem;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "INSERT_ALLOWED")
    private String insertAllowed;

    @Column(name = "UPDATE_ALLOWED")
    private String updateAllowed;

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    private Instant dateCreated;

    @NotNull
    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private Instant dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

}