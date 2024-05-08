package gov.uk.courtdata.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

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

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private Instant dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

}