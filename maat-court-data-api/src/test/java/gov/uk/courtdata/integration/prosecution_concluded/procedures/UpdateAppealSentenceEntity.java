package gov.uk.courtdata.integration.prosecution_concluded.procedures;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "UPDATE_APPEAL_SENTENCE", schema = "TOGDATA")
public class UpdateAppealSentenceEntity {
    @Id
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    @Column(name = "DB_USER", nullable = false)
    private String dbUser;
    @Column(name = "SENTENCE_DATE", nullable = false)
    private LocalDate sentenceDate;
    @Column(name = "DATE_CHANGED", nullable = false)
    private LocalDate dateChanged;

}
