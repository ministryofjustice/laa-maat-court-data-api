package gov.uk.courtdata.link.service;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JobStatus;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CreateLinkCpJobStatusService {

    private final WqCoreRepository wqCoreRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void execute(CpJobStatus cpJobStatus) {

        final Integer maatId = cpJobStatus.getMaatId();

        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository.findBymaatId(maatId)
                .stream().findFirst()
                .orElseThrow(() -> {
                    throw new MAATCourtDataException(format("No Record found for Maat ID - %s ", maatId));
                });

        final Integer transactionId = wqLinkRegisterEntity.getCreatedTxId();
        Optional<WqCoreEntity> optionalWqCoreEntity = wqCoreRepository.findById(transactionId);

        WqCoreEntity wqCoreEntity = optionalWqCoreEntity.orElseThrow(() -> {
            throw new MAATCourtDataException(format("No Record found for Maat ID- %s, Txn ID-%s", maatId, transactionId));
        });

        wqCoreEntity.setWqStatus(
                cpJobStatus.getJobStatus() == JobStatus.SUCCESS ? WQStatus.SUCCESS.value() : WQStatus.FAIL.value());

        wqCoreRepository.save(wqCoreEntity);
    }
}
