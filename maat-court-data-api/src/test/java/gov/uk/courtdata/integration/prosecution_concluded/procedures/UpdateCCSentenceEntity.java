package gov.uk.courtdata.integration.prosecution_concluded.procedures;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "UPDATE_CC_SENTENCE", schema = "TOGDATA")
public class UpdateCCSentenceEntity {
    @Id
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    @Column(name = "DB_USER", nullable = false)
    private String dbUser;
    @Column(name = "SENTENCE_DATE", nullable = false)
    private LocalDate sentenceDate;
}
