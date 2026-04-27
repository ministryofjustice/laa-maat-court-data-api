package gov.uk.courtdata.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.reporder.MaatSearchRequest;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class RepOrderRepositoryTest {

  @Autowired
  private RepOrderRepository repository;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  void setUp() {
    Applicant applicant = Applicant.builder()
        .firstName("John")
        .lastName("Smith")
        .dob(LocalDate.of(1990, 1, 1))
        .niNumber("AB123456C")
        .build();
    entityManager.persist(applicant);

    RepOrderEntity repOrder = RepOrderEntity.builder()
        .applicationId(applicant.getId())
        .arrestSummonsNo("ASN123")
        .committalDate(LocalDate.of(2024, 1, 1))
        .catyCaseType("ABC")
        .build();
    entityManager.persist(repOrder);

    entityManager.flush();
  }

  @Test
  void findRepId_shouldReturnResult_whenDobIsNull() {
    MaatSearchRequest request = MaatSearchRequest.builder()
        .firstName("John")
        .lastName("Smith")
        .asn("ASN123")
        .dob(null)
        .build();

    Set<Integer> result = repository.findRepId(request);

    assertThat(result).isNotEmpty();
  }

  @Test
  void findRepId_returnsEmpty_whenDobProvidedButDoesNotMatch() {
    MaatSearchRequest req = MaatSearchRequest.builder()
        .firstName("John")
        .lastName("Smith")
        .asn("ASN123")
        .dob(LocalDate.of(2000, 1, 1))
        .build();

    assertThat(repository.findRepId(req)).isEmpty();
  }

}
