package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ReservationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservationsRepository extends JpaRepository<ReservationsEntity,String> { }
