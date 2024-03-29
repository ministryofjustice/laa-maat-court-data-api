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

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_HEARING",schema = "MLA")
public class WQHearingEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;

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
    @Column(name = "CASE_URN")
    private String caseUrn;
    @Column(name = "RESULT_CODES")
    private String resultCodes;
}