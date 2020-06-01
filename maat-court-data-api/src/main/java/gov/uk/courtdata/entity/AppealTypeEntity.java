package gov.uk.courtdata.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
