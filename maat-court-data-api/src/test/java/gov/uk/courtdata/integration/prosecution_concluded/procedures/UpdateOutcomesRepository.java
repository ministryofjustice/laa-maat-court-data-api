package gov.uk.courtdata.integration.prosecution_concluded.procedures;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateOutcomesRepository extends JpaRepository<UpdateOutcomesEntity, String> {
}
