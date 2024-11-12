package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FeatureToggleEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureToggleRepository extends JpaRepository<FeatureToggleEntity, Integer> {

  /**
   * Gets the feature toggles for a user, which is a combination of user-specific feature toggles,
   * in addition to those which are applicable to all users through a null username.
   * @param username The username to get feature toggles for.
   * @return A list of feature toggles applicable to the user.
   */
  @Query(value = "select ID, USER_NAME, FEATURE, ACTION, IS_ENABLED from TOGDATA.FEATURE_TOGGLE where USER_NAME = :username or USER_NAME is null", nativeQuery = true)
  List<FeatureToggleEntity> getAllFeatureTogglesForUser(@Param("username") String username);
}
