package gov.uk.courtdata.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REP_ORDER_COMMON_PLATFORM_DATA", schema = "TOGDATA")
public class RepOrderCPDataEntity {

    @Id
    @Column(name = "REP_ORDER_ID")
    private Integer repOrderId;
    @Column(name = "CASE_URN")
    private String caseUrn;
    @Column(name = "DEFENDANT_ID")
    private String defendantId;
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "IN_COMMON_PLATFORM")
    private String inCommonPlatform;
}
