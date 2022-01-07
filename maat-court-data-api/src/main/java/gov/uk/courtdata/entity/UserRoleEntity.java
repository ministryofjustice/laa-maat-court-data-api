package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.UserRoleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserRoleId.class)
@Table(name = "USER_ROLES", schema = "TOGDATA")
public class UserRoleEntity {
    @Id
    @Column(name = "USER_NAME")
    private String username;

    @Id
    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_ROLE_ENABLED")
    private String userRoleEnabled;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
}
