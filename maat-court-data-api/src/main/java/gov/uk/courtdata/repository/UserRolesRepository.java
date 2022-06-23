package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.UserRoleEntity;
import gov.uk.courtdata.model.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoleEntity, UserRoleId> {
}
