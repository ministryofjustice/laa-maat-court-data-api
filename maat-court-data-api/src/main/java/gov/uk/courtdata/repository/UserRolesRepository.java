package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.UserRoleEntity;
import gov.uk.courtdata.model.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoleEntity, UserRoleId> {
}
