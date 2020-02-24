package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REP_ORDER_COMMON_PLATFORM_DATA", schema = "TOGDATA")
public class RepOrderCommonPlatformDataEntity {

    @Id
    @Column(name = "REP_ORDER_ID")
    private Integer repOrderId;
    @Column(name = "CASE_URN")
    private String caseUrn;
    @Column(name = "DEFENDANT_ID")
    private String defendantId;
    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;

}
