package gov.uk.courtdata.entity;


import gov.uk.courtdata.model.CourtHouseCodesId;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CourtHouseCodesId.class)
@Table(name = "XXMLA_XLAT_COURTHOUSE_CODES", schema = "MLA")
public class CourtHouseCodesEntity {

    @Id
    @Column(name = "CODE")
    private String code;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "COURTHOUSE_CODE")
    private String courtHouseCode;
    @Column(name = "cjsAreaFullCode")
    private String cjsAreaFullCode;
    @Id
    @Column(name = "EFFECTIVE_FROM_DATE")
    private LocalDateTime effectiveDateFrom;
    @Column(name="EFFECTIVE_TO_DATE")
    private LocalDateTime effectiveToDate;
    @Column(name = "SORT_POLICY_ORDER")
    private String sortPolicyOrder;
    @Column(name="MODIFIED_USER")
    private String modifierUser;
    @Column(name = "MODIFIED_DATE")
    private String modifiedDate;

}

