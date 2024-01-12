package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.reporder.projection.IOJAssessorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RepOrderRepository extends JpaRepository<RepOrderEntity, Integer>, JpaSpecificationExecutor<RepOrderEntity> {

    @Query(value = """
               SELECT
                   NVL(INITCAP(U.FIRST_NAME
                               || ' '
                               || U.SURNAME),
                       R.USER_CREATED) name,
                       R.USER_CREATED userName
               FROM
                   REP_ORDERS R
                   LEFT JOIN USERS U ON (U.USER_NAME = R.USER_CREATED)
               WHERE
                   ID = :repOrderId
            """,
            nativeQuery = true)
    IOJAssessorDetails findIOJAssessorDetails(int repOrderId);
}
