package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.ConfigParametersId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONFIG_PARAMETERS", schema = "TOGDATA")
@IdClass(ConfigParametersId.class)
public class ConfigParametersEntity {

    @Id
    @Column(name = "CODE")
    private String code;

    @Id
    @Column(name = "EFFECTIVE_DATE")
    private LocalDateTime effectiveDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VALUE")
    private String value;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @CreationTimestamp
    @Column(name = "TIME_STAMP")
    private LocalDateTime timestamp;

    @Column(name = "USER_MODIFIED")
    private String userModified;

}
