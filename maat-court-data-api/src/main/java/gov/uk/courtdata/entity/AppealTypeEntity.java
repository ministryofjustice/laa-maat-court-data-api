package gov.uk.courtdata.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "APPEAL_TYPES", schema = "TOGDATA")
public class AppealTypeEntity {

    @Id
    @Column(name = "CODE")
    private String code;
}
