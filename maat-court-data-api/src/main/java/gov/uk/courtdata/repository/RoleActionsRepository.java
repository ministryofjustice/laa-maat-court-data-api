package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.RoleActionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleActionsRepository extends JpaRepository<RoleActionsEntity, Integer> {
    @Query(value = "select ra from RoleActionsEntity ra inner join UserRoleEntity ur on (ra.roleName = ur.roleName) where  ur.username = :username and ra.action = :action and ra.enabled = 'Y'")
    public Optional<RoleActionsEntity> getRoleAction(@Param("username") String username, @Param("action") String action);
}
