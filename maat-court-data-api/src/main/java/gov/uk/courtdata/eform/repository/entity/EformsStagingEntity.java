package gov.uk.courtdata.eform.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Clob;


@Builder
@Data
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

    @Column(name = "MAAT_REF")
    private Integer maatRef;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "XML_DOC")
    @Lob
    private String xmlDoc;
}
