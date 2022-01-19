package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "USERS", schema = "TOGDATA")
public class UserEntity {

    @Id
    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PASSWORD_EXPIRY")
    private LocalDateTime passwordExpiry;

    @Column(name = "AREA_ID")
    private Integer areaId;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "INITIALS")
    private String initials;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "LOCKED")
    private String locked;

    @Column(name = "LOGGED_IN")
    private String loggedIn;

    @Column(name = "LOGGING_IN_ATTEMPTS")
    private Integer loggingInAttempts;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "DATE_LAST_LOGIN")
    private LocalDateTime lastLoginDate;

    @Column(name = "MAX_LOGIN_ATTEMPTS")
    private Integer maxLoginAttempts;

    @Column(name = "CURRENT_SESSION")
    private String currentSession;
}
