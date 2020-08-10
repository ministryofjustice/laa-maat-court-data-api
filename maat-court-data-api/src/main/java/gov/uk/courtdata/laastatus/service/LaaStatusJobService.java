package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.enums.JobStatus;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.laastatus.LaaStatusJob;
import gov.uk.courtdata.repository.WqCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class LaaStatusJobService {

    private final WqCoreRepository wqCoreRepository;

    public void execute(LaaStatusJob laaStatusJob) {

        final Integer transactionId = laaStatusJob.getLaaStatusTransactionId();
        final Integer maatId = laaStatusJob.getMaatId();
        Optional<WqCoreEntity> optionalWqCoreEntity = wqCoreRepository.findById(transactionId);

        WqCoreEntity wqCoreEntity = optionalWqCoreEntity.orElseThrow(() -> {

            throw new MAATCourtDataException(format("No Record found for Maat ID- %s," +
                    " Txn ID-%s", maatId, transactionId));
        });

        wqCoreEntity.setWqStatus(
                laaStatusJob.getJobStatus() == JobStatus.SUCCESS ? WQStatus.SUCCESS.value()
                        : WQStatus.FAIL.value());

        wqCoreRepository.save(wqCoreEntity);

    }
}
