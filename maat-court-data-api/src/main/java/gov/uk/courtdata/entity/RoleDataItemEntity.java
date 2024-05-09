package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    
}