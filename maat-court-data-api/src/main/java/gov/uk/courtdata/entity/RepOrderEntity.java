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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REP_ORDERS", schema = "TOGDATA")
public class RepOrderEntity {

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CATY_CASE_TYPE")
    private String catyCaseType;
    @Column(name = "APTY_CODE")
    private String aptyCode;
}
