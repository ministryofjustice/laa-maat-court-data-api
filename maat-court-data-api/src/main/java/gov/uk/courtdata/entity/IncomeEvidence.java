package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "INCOME_EVIDENCE", schema = "TOGDATA" )
public class IncomeEvidence {
    @Id
    @Column(name = "EVIDENCE", nullable = false, length = 20)
    private String id;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "DATE_CREATED", nullable = false)
    private Instant dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private Instant dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "LETTER_DESCRIPTION", length = 500)
    private String letterDescription;

    @Column(name = "WELSH_LETTER_DESCRIPTION", length = 500)
    private String welshLetterDescription;

    @Column(name = "ADHOC", length = 1)
    private String adhoc;

}