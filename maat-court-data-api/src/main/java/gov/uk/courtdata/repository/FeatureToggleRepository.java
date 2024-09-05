package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FeatureToggleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureToggleRepository extends JpaRepository<FeatureToggleEntity, Integer> {

  @Query(value = "select ID, USER_NAME, FEATURE, ACTION from TOGDATA.FEATURE_TOGGLE where USER_NAME = :username or USER_NAME is null", nativeQuery = true)
  List<FeatureToggleEntity> getFeatureTogglesForUser(@Param("username") String username);
}
