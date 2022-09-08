package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "INCOME_EVIDENCE", schema = "TOGDATA" )
public class IncomeEvidence {
    @Id
    @Column(name = "EVIDENCE", nullable = false, length = 20)
    private String id;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "LETTER_DESCRIPTION", length = 500)
    private String letterDescription;

    @Column(name = "WELSH_LETTER_DESCRIPTION", length = 500)
    private String welshLetterDescription;

    @Column(name = "ADHOC", length = 1)
    private String adhoc;

}