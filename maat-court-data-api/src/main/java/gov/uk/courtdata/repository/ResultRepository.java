package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.id.AsnSeqTxnCaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<ResultEntity, AsnSeqTxnCaseId> {
}
