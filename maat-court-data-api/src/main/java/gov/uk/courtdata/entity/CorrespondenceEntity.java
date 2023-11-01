package gov.uk.courtdata.entity;

import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CORRESPONDENCE", schema = "TOGDATA")
public class CorrespondenceEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    @Column(name = "GENERATE_DATE", nullable = false)
    private LocalDateTime generateDate;
    @Column(name = "PRINT_DATE")
    private LocalDateTime printDate;
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false)
    private String userCreated;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private LocalDateTime userModified;
    @Column(name = "COTY_CORRESPONDENCE_TYPE")
    private String cotyCorresType;
    @Column(name = "FIAS_ID")
    private Integer fiasId;
    @Column(name = "PAAS_ID")
    private Integer passId;
    @Column(name = "CLAY_ID")
    private Integer clayId;
    @Column(name = "SE_HISTORY_ID")
    private Integer seHistoryId;

}
