package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ContribCalcParametersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ContribCalcParametersRepository extends JpaRepository<ContribCalcParametersEntity, LocalDateTime> {

    @Query(value = "SELECT * FROM TOGDATA.CONTRIBS_CALC_PARAMETERS WHERE FROM_DATE <= :effectiveDate AND nvl(TO_DATE,'01-JAN-2999') > :effectiveDate", nativeQuery = true)
    ContribCalcParametersEntity findCurrentContribCalcParameters(String effectiveDate);

}