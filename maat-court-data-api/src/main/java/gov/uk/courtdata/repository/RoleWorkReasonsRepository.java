package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RoleWorkReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleWorkReasonsRepository extends JpaRepository<RoleWorkReasonEntity, Integer> {
    @Query(value = "select wr.* from ROLE_WORK_REASONS wr inner join USER_ROLES ur on (wr.ROLE_NAME = ur.ROLE_NAME) where  ur.USER_NAME = :username and wr.NWOR_CODE = :nworCode FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    Optional<RoleWorkReasonEntity> getNewWorkReason(@Param("username") String username, @Param("nworCode") String nworCode);
}
