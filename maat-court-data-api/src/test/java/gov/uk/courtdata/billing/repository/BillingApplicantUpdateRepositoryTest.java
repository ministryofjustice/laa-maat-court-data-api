package gov.uk.courtdata.billing.repository;

import gov.uk.courtdata.billing.entity.BillingApplicantUpdateEntity;
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
class BillingApplicantUpdateRepositoryTest {

    @Autowired
    private BillingApplicantUpdateRepository updateRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void shouldResetSendToCclfFlag() {
        BillingApplicantUpdateEntity entity = BillingApplicantUpdateEntity.builder()
                .id(1)
                .sendToCclf("Y")
                .dateModified(LocalDateTime.now())
                .userModified("initial_user")
                .build();

        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();

        int updatedRows = updateRepository.resetApplicantBilling(List.of(1), "system_user");

        assertThat(updatedRows).isEqualTo(1);

        entityManager.flush();
        entityManager.clear();

        BillingApplicantUpdateEntity updated = entityManager.find(BillingApplicantUpdateEntity.class, 1);
        assertThat(updated.getSendToCclf()).isNull();
        assertThat(updated.getUserModified()).isEqualTo("system_user");
        assertThat(updated.getDateModified()).isNotNull();
    }
}