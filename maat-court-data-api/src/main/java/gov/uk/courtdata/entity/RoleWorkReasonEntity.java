package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE_WORK_REASONS", schema = "TOGDATA")
public class RoleWorkReasonEntity {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NWOR_CODE", nullable = false)
    private String nworCode;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;
}
