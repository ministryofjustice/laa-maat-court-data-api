package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(statements = {
        "CREATE SCHEMA IF NOT EXISTS TOGDATA;",
        """
        CREATE TABLE IF NOT EXISTS TOGDATA.APPLICANTS (
            ID INT PRIMARY KEY,
            SEND_TO_CCLF VARCHAR(1),
            DATE_CREATED TIMESTAMP,
            USER_CREATED VARCHAR(100),
            DATE_MODIFIED TIMESTAMP,
            USER_MODIFIED VARCHAR(100)
        );
        """
})
class BillingApplicantRepositoryTest {

    @Autowired
    private BillingApplicantRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void shouldResetSendToCclfFlag() {
        BillingApplicantEntity entity = new BillingApplicantEntity();
        entity.setId(1);
        entity.setSendToCclf("Y");
        entity.setDateCreated(LocalDateTime.now());
        entity.setUserCreated("test_user");
        repository.save(entity);

        int updatedRows = repository.resetApplicantBilling(List.of(1), "system_user");
        System.out.println("Rows updated: " + updatedRows);

        entityManager.flush();
        entityManager.clear();

        BillingApplicantEntity updated = repository.findById(1).orElseThrow();
        assertThat(updated.getSendToCclf()).isNull();
        assertThat(updated.getUserModified()).isEqualTo("system_user");
    }
}