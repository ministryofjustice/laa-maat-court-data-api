package gov.uk.courtdata.eform.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EFORMS_STAGING", schema = "HUB")
public class EformsStagingEntity {

    @Id
    @Column(name = "USN")
    private Integer usn;

    @Column(name = "TYPE")
    private String type;
}
