package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE_ACTIONS", schema = "TOGDATA")
public class RoleActionsEntity {
    @Id
    @Column(name = "ID")
    private Integer Id;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "ENABLED")
    private String enabled;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;
}
