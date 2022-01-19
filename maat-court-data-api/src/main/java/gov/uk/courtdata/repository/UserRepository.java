package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
