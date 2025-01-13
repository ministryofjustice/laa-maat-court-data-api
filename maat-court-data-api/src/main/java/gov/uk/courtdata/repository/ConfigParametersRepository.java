package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ConfigParametersEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigParametersRepository extends JpaRepository<ConfigParametersEntity, String> {
    @Query(value = "SELECT * FROM TOGDATA.CONFIG_PARAMETERS", nativeQuery = true)
    List<ConfigParametersEntity> retrieveAll();

    @Query(value = "SELECT * from TOGDATA.CONFIG_PARAMETERS WHERE CODE = :code AND EFFECTIVE_DATE = :effectiveData", nativeQuery = true)
    Optional<ConfigParametersEntity> findConfigParametersEntityByCodeAndEffectiveDate(String code, LocalDate effectiveData);
}
