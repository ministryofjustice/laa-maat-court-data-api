package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "CROWN_COURTS", schema = "TOGDATA")
public class CrownCourtCode {
    @Id
    @Column(name = "CODE")
    private String code;
    @Column(name = "OU_CODE")
    private String ouCode;

}
