package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.RoleActionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleActionsRepository extends JpaRepository<RoleActionsEntity, Integer> {
    @Query(value = "select ra.* from ROLE_ACTIONS ra inner join USER_ROLES ur on (ra.ROLE_NAME = ur.ROLE_NAME) where  ur.USER_NAME = :username and ra.ACTION = :action and ra.enabled = 'Y' FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<RoleActionsEntity> getRoleAction(@Param("username") String username, @Param("action") String action);
}
