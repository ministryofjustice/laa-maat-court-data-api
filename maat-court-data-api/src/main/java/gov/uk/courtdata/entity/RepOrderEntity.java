package gov.uk.courtdata.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
    @Column(name = "CASE_ID")
    private String caseId;
    @Column(name = "CATY_CASE_TYPE")
    private String catyCaseType;
    @Column(name = "APTY_CODE")
    private String aptyCode;
    @Column(name = "ARREST_SUMMONS_NO")
    private String arrestSummonsNo;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

}
