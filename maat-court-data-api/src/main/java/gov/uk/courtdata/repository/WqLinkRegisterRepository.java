package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WqLinkRegisterRepository extends JpaRepository<WqLinkRegisterEntity,Integer> {

}
