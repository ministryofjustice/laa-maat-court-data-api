package gov.uk.courtdata.billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "applicants", schema = "TOGDATA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingApplicantUpdateEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "send_to_cclf")
    private String sendToCclf;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "user_modified")
    private String userModified;
}