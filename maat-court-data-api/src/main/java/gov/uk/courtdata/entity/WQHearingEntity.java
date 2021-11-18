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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_HEARING",schema = "MLA")
public class WQHearingEntity {

    @Id
    @Column(name = "HEARING_UUID")
    private String hearingUUID;
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "WQ_JURISDICTION_TYPE")
    private String wqJurisdictionType;
    @Column(name= "OU_COURT_LOCATION")
    private String ouCourtLocation;
    @Column(name = "CREATED_DATE_TIME",nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    @Column(name = "UPDATED_DATE_TIME")
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
}
