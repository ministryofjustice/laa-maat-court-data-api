package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FdcItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FdcItemsRepository extends JpaRepository<FdcItemsEntity, Integer> {

    List<FdcItemsEntity> findByFdcIdIn(List<Integer> ids);

}