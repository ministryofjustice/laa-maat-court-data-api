package gov.uk.courtdata.billing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
