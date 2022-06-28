package gov.uk.courtdata.laastatus.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.enums.JobStatus;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.repository.WqCoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@XRayEnabled
@RequiredArgsConstructor
public class LaaStatusJobService {

    private final WqCoreRepository wqCoreRepository;

    public void execute(CpJobStatus cpJobStatus) {

        final Integer transactionId = cpJobStatus.getLaaStatusTransactionId();
        final Integer maatId = cpJobStatus.getMaatId();
        Optional<WqCoreEntity> optionalWqCoreEntity = wqCoreRepository.findById(transactionId);

        WqCoreEntity wqCoreEntity = optionalWqCoreEntity.orElseThrow(() -> {

            throw new MAATCourtDataException(format("No Record found for Maat ID- %s," +
                    " Txn ID-%s", maatId, transactionId));
        });

        wqCoreEntity.setWqStatus(
                cpJobStatus.getJobStatus() == JobStatus.SUCCESS ? WQStatus.SUCCESS.value()
                        : WQStatus.FAIL.value());

        wqCoreRepository.save(wqCoreEntity);

    }
}
