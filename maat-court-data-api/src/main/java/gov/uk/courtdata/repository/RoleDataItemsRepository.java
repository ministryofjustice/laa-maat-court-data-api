package gov.uk.courtdata.repository;


import gov.uk.courtdata.entity.RoleDataItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDataItemsRepository extends JpaRepository<RoleDataItemEntity, Integer> {

    @Query(value = "select rdi.* from TOGDATA.USER_ROLES ur inner join TOGDATA.ROLE_DATA_ITEMS rdi on ur.ROLE_NAME=rdi.ROLE_NAME " +
            "where  ur.USER_NAME = :username", nativeQuery = true)
    Optional<List<RoleDataItemEntity>> getRoleDataItemsForUser(@Param("username") String username);

}
