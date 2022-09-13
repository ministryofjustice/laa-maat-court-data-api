package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class HearingsService {

    private final WQHearingRepository wqHearingRepository;

    private final CourtDataAdapterClient courtDataAdapterClient;

    private final ProsecutionConcludedDataService prosecutionConcludedDataService;

    public WQHearingEntity retrieveHearingForCaseConclusion(ProsecutionConcluded prosecutionConcluded) {
        WQHearingEntity hearing = getWqHearingEntity(prosecutionConcluded);

        if (hearing == null && prosecutionConcluded.isConcluded()) {
            triggerHearingDataProcessing(prosecutionConcluded);
            prosecutionConcludedDataService.execute(prosecutionConcluded);
        }

        return hearing;
    }

    private WQHearingEntity getWqHearingEntity(ProsecutionConcluded prosecutionConcluded) {
        List<WQHearingEntity> wqHearingEntityList = wqHearingRepository
                .findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(), prosecutionConcluded.getHearingIdWhereChangeOccurred().toString());
        return !wqHearingEntityList.isEmpty() ? wqHearingEntityList.get(0) : null;
    }

    private void triggerHearingDataProcessing(ProsecutionConcluded prosecutionConcluded) {
        try {
            courtDataAdapterClient.triggerHearingProcessing(
                    prosecutionConcluded.getHearingIdWhereChangeOccurred(),
                    prosecutionConcluded.getMetadata().getLaaTransactionId());
        } catch (MAATCourtDataException exception) {
            log.info(exception.getMessage());
        }
    }


}
