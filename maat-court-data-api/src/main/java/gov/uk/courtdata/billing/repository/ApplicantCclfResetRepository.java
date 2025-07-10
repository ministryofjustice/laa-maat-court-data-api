package gov.uk.courtdata.billing.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class ApplicantCclfResetRepository {

    private static final String RESET_CCLF_SQL = """
        UPDATE APPLICANTS
        SET SEND_TO_CCLF = NULL,
            DATE_MODIFIED = SYSDATE,
            USER_MODIFIED = USER
        WHERE ID IN (SELECT APPL_ID FROM MAAT_REFS_TO_EXTRACT)
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public void resetApplicantCclfFlag() {
        jdbcTemplate.update(RESET_CCLF_SQL, Collections.emptyMap());
    }
}