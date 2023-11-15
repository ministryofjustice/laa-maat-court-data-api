package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.RoleActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleActionsRepository extends JpaRepository<RoleActionEntity, Integer> {
    @Query(value = "select ra.* from TOGDATA.ROLE_ACTIONS ra inner join TOGDATA.USER_ROLES ur on (ra.ROLE_NAME = ur.ROLE_NAME) where  ur.USER_NAME = :username and ra.ACTION = :action and ra.enabled = 'Y' FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<RoleActionEntity> getRoleAction(@Param("username") String username, @Param("action") String action);

    @Query(value = "select DISTINCT(ra.ACTION) from TOGDATA.ROLE_ACTIONS ra inner join TOGDATA.USER_ROLES ur on (ra.ROLE_NAME = ur.ROLE_NAME) " +
            "where  ur.USER_NAME = :username", nativeQuery = true)
    Optional<List<String>> getRoleActionsForUser(@Param("username") String username);

}
